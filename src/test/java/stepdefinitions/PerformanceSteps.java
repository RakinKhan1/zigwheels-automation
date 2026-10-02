package stepdefinitions;

import io.cucumber.java.en.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import utils.DriverFactory;
import utils.PerformanceUtils;
import utils.PerformanceUtils.PageTiming;

/**
 * Key Automation Scope: "Capture some performance metrics like Response
 * time, Throughput, Error rate etc."
 *
 * This step captures response times for real page loads on every run.
 * Throughput and error rate under load come from the JMeter test in
 * evidence/jmeter (see PERFORMANCE.md), because a single Selenium browser
 * cannot generate load.
 */
public class PerformanceSteps {

    private static final Logger log = LoggerFactory.getLogger(PerformanceSteps.class);

    /** Server response time budget used for reporting (not for failing). */
    private static final long SERVER_RESPONSE_BUDGET_MS = 1000;

    @And("I capture the page load performance metrics for the {string} page")
    public void i_capture_the_page_load_performance_metrics(String pageName) {
        PageTiming t = PerformanceUtils.capture(DriverFactory.getDriver(), pageName);

        // Fails only if the browser reported no usable timings at all,
        // i.e. the measurement itself did not work.
        Assert.assertTrue(t.serverResponseMs() >= 0 && t.domReadyMs() > 0,
                "Page load timings were not captured for " + pageName + ": " + t);

        String text = "Page load performance: " + pageName + " (" + t.url() + ")\n"
                + "  Server response time: " + t.serverResponseMs() + " ms"
                + (t.serverResponseMs() <= SERVER_RESPONSE_BUDGET_MS
                    ? "  (within " + SERVER_RESPONSE_BUDGET_MS + " ms budget)"
                    : "  [over " + SERVER_RESPONSE_BUDGET_MS + " ms budget]") + "\n"
                + "  Page usable (DOM ready): " + t.domReadyMs() + " ms\n"
                + "  Fully loaded: " + (t.fullLoadMs() > 0 ? t.fullLoadMs() + " ms"
                    : "still loading third-party content after 10 s") + "\n"
                + "  Saved to target/performance/page-load-timings.csv";
        log.info("\n{}", text);
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log(text);
        }
    }
}
