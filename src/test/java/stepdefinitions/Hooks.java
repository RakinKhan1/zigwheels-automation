package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DriverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Per-scenario lifecycle. Each Cucumber scenario gets its own driver via
 * DriverFactory's ThreadLocal, so scenarios running in parallel TestNG
 * threads never see each other's browser session. A failing scenario
 * gets a screenshot attached straight to the report - the fastest way to
 * see WHY something failed without re-running it.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private static final ThreadLocal<Scenario> CURRENT_SCENARIO = new ThreadLocal<>();

    /**
     * The scenario running on this thread, so step classes can write
     * findings (e.g. accessibility results) straight into the Cucumber and
     * Extent reports with scenario.log(), not just the console.
     */
    public static Scenario currentScenario() {
        return CURRENT_SCENARIO.get();
    }

    /**
     * Fills in the "Environment" panel of the Allure report, which reads
     * target/allure-results/environment.properties.
     */
    @BeforeAll
    public static void writeAllureEnvironment() {
        try {
            Path dir = Path.of("target", "allure-results");
            Files.createDirectories(dir);
            String env = String.join("\n",
                    "Site=" + utils.ConfigReader.get("base.url"),
                    "Browser=" + utils.ConfigReader.get("browser", "chrome"),
                    "Headless=" + utils.ConfigReader.get("headless", "false"),
                    "Parallel.threads=3",
                    "Framework=Selenium + Cucumber + TestNG",
                    "Java=" + System.getProperty("java.version"),
                    "OS=" + System.getProperty("os.name")) + "\n";
            Files.writeString(dir.resolve("environment.properties"), env);
        } catch (IOException e) {
            log.warn("Could not write Allure environment.properties", e);
        }
    }

    @Before
    public void setUp(Scenario scenario) {
        CURRENT_SCENARIO.set(scenario);
        log.info("Starting scenario: {}", scenario.getName());
        DriverFactory.getDriver(); // eagerly initialise so pages can use it immediately
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName().replaceAll("\\s+", "_"));

            // Also save as a plain PNG file, independent of whichever Cucumber
            // report format is configured - the basic "html" plugin doesn't
            // reliably render attached images inline, so this is the version
            // that's actually easy to open and check.
            try {
                Path dir = Path.of("target", "failure-screenshots");
                Files.createDirectories(dir);
                String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9-]", "_");
                File out = dir.resolve(safeName + ".png").toFile();
                Files.write(out.toPath(), screenshot);
                log.error("Scenario FAILED: {} - screenshot saved to {}", scenario.getName(), out.getAbsolutePath());
            } catch (IOException e) {
                log.error("Could not save failure screenshot to a plain file", e);
            }
        } else {
            // Final-state screenshot for passing scenarios too, so the
            // Cucumber, Extent and Allure reports all show what the browser
            // ended on, not just the log text.
            try {
                byte[] finalState = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(finalState, "image/png", "Final state");
            } catch (Exception screenshotIssue) {
                log.warn("Could not capture final-state screenshot for {}", scenario.getName());
            }
            log.info("Scenario passed: {}", scenario.getName());
        }
        DriverFactory.quitDriver();
        CURRENT_SCENARIO.remove();
    }
}
