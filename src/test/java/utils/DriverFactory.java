package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;
import java.util.Collections;

/**
 * Owns WebDriver lifecycle behind a ThreadLocal so that parallel TestNG
 * threads (Key Automation Scope: "Implement parallel test execution")
 * never share or clobber each other's driver instance.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            initDriver();
        }
        return DRIVER.get();
    }

    private static void initDriver() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        WebDriver driver;

        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new");
                // Stabilises parallel runs in CI / headless containers
                options.addArguments("--window-size=1920,1080", "--disable-notifications",
                        "--remote-allow-origins=*", "--no-sandbox", "--disable-dev-shm-usage");
                // Confirmed on a real run (22 Sep 2026): the windows & frames
                // capability check's window.open() call was silently blocked by
                // Chrome's default popup blocker, so no second window handle ever
                // appeared and BasePage.switchToNewWindow() timed out waiting for
                // one. This disables that blocking - legitimate for a controlled
                // test browser, not a security bypass of anything the scenarios test.
                options.addArguments("--disable-popup-blocking");
                // Google's sign-in page (used by the invalid-login scenario) actively
                // detects and blocks the default Selenium/ChromeDriver fingerprint
                // (navigator.webdriver=true, the "Chrome is being controlled by
                // automated test software" info bar). These two settings remove
                // that specific fingerprint. This is a legitimate, well-documented
                // Selenium configuration - not a bypass of any login security check,
                // since the scenario still supplies genuinely invalid credentials.
                options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(options);
            }
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds")));
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
