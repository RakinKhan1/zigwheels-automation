package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Page load timings from the browser's own Navigation Timing API
 * (performance.getEntriesByType('navigation')), captured inside the
 * Selenium suite on every run.
 *
 * This measures what one real user's browser experienced for one page
 * load. It complements, and does not replace, the JMeter load test in
 * evidence/jmeter, which measures throughput and error rate under 50
 * concurrent users.
 */
public class PerformanceUtils {

    private static final Logger log = LoggerFactory.getLogger(PerformanceUtils.class);
    private static final Path CSV = Path.of("target", "performance", "page-load-timings.csv");

    private PerformanceUtils() {
    }

    /**
     * @param serverResponseMs time from sending the request to the first byte
     *                         of the response (server response time)
     * @param domReadyMs       time until the HTML was parsed and the page was
     *                         usable (DOMContentLoaded)
     * @param fullLoadMs       time until the page and all its resources
     *                         finished loading (load event); -1 if the page was
     *                         still loading adverts etc. when measured
     */
    public record PageTiming(String page, String url, long serverResponseMs, long domReadyMs, long fullLoadMs) {
    }

    private static final String NAV_TIMING_JS =
            "var n = performance.getEntriesByType('navigation')[0];" +
            "if (!n) return null;" +
            "return {req: n.requestStart, resp: n.responseStart," +
            "        dcl: n.domContentLoadedEventEnd, load: n.loadEventEnd};";

    public static PageTiming capture(WebDriver driver, String pageName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<?, ?> t = null;
        // Wait up to 10 seconds for the load event; ad-heavy pages can keep
        // loading third-party content for a while after they are usable.
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            Object raw = js.executeScript(NAV_TIMING_JS);
            if (raw instanceof Map<?, ?> map) {
                t = map;
                if (number(map.get("load")) > 0) {
                    break;
                }
            }
            sleep(250);
        }
        if (t == null) {
            throw new IllegalStateException("Browser returned no Navigation Timing data for " + pageName);
        }
        long serverResponse = Math.round(number(t.get("resp")) - number(t.get("req")));
        long domReady = Math.round(number(t.get("dcl")));
        long fullLoad = number(t.get("load")) > 0 ? Math.round(number(t.get("load"))) : -1;

        PageTiming timing = new PageTiming(pageName, driver.getCurrentUrl(), serverResponse, domReady, fullLoad);
        appendToCsv(timing);
        return timing;
    }

    private static double number(Object value) {
        return value instanceof Number n ? n.doubleValue() : 0;
    }

    private static synchronized void appendToCsv(PageTiming t) {
        try {
            Files.createDirectories(CSV.getParent());
            if (!Files.exists(CSV)) {
                Files.writeString(CSV, "timestamp,page,url,server_response_ms,dom_ready_ms,full_load_ms\n");
            }
            String row = String.join(",",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    "\"" + t.page() + "\"", t.url(),
                    String.valueOf(t.serverResponseMs()), String.valueOf(t.domReadyMs()),
                    String.valueOf(t.fullLoadMs())) + "\n";
            Files.writeString(CSV, row, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Could not write page timing to {}", CSV, e);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
