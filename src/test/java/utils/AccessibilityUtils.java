package utils;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wraps axe-core so a single call gives every step definition a real
 * accessibility scan of the current page (Key Automation Scope item:
 * "basic accessibility test like Screen reader compatibility, Keyboard
 * only accessibility etc.").
 *
 * axe-core covers screen-reader-relevant checks itself (missing alt text,
 * ARIA misuse, unlabelled form fields, colour contrast, heading order).
 * Keyboard-only access is checked separately in {@link #tabThroughPage},
 * because axe-core's static analysis cannot press Tab through the page -
 * that needs real key presses sent through WebDriver, which that method does.
 */
public class AccessibilityUtils {

    private static final Logger log = LoggerFactory.getLogger(AccessibilityUtils.class);
    private static final Path REPORT_DIR = Paths.get("target", "accessibility-reports");

    private AccessibilityUtils() {
    }

    /**
     * Runs a full axe-core scan of the page currently loaded in {@code driver}
     * and writes a timestamped JSON report to target/accessibility-reports/.
     * Returns the list of rule violations found (empty list = clean page).
     */
    public static List<Rule> scanPage(WebDriver driver, String pageLabel) {
        Results results = new AxeBuilder().analyze(driver);
        writeReport(results, pageLabel);

        List<Rule> violations = results.getViolations();
        if (violations.isEmpty()) {
            log.info("Accessibility scan clean for [{}] - 0 violations", pageLabel);
        } else {
            log.warn("Accessibility scan for [{}] found {} violation type(s):", pageLabel, violations.size());
            for (Rule v : violations) {
                log.warn("  [{}] {} - {} node(s) affected", v.getImpact(), v.getId(), v.getNodes().size());
            }
        }
        return violations;
    }

    /** One stop on the keyboard Tab journey through a page. */
    public record FocusStop(int press, WebElement element, String description,
                            boolean interactive, boolean visible, boolean focusIndicator,
                            boolean hasAccessibleName) {
    }

    // Reads whatever currently has keyboard focus. Returns null when focus
    // is still on <body> (i.e. the Tab key did not move focus anywhere).
    private static final String FOCUS_INFO_JS =
            "var el = document.activeElement;" +
            "if (!el || el === document.body || el === document.documentElement) return null;" +
            "var tag = el.tagName.toLowerCase();" +
            "var role = el.getAttribute('role') || '';" +
            "var label = (el.getAttribute('aria-label') || el.innerText || el.value || el.getAttribute('title')" +
            "  || el.getAttribute('alt') || el.getAttribute('placeholder') || '').toString().trim().replace(/\\s+/g, ' ').substring(0, 60);" +
            "var interactive = ['a','button','input','select','textarea','iframe','summary'].indexOf(tag) >= 0" +
            "  || role !== '' || el.isContentEditable;" +
            "var r = el.getBoundingClientRect();" +
            "var visible = r.width > 0 && r.height > 0;" +
            "var cs = getComputedStyle(el);" +
            "var focusIndicator = (cs.outlineStyle !== 'none' && parseFloat(cs.outlineWidth) > 0)" +
            "  || (cs.boxShadow && cs.boxShadow !== 'none');" +
            "return {el: el, tag: tag, role: role, label: label, interactive: interactive," +
            "  visible: visible, focusIndicator: focusIndicator};";

    /**
     * Keyboard-only accessibility check. Presses the real Tab key
     * {@code presses} times from the top of the page, exactly as a
     * keyboard-only user would, and records where focus lands after each
     * press: which element, whether it is a genuinely interactive control,
     * whether it is visible on screen, and whether it shows a visible focus
     * indicator (outline or box-shadow) so a sighted keyboard user can see
     * where they are.
     *
     * Unlike calling element.sendKeys() or focus() in code, this only moves
     * focus the way the browser's own Tab order allows, so an element that
     * is unreachable by keyboard is genuinely never reached here.
     */
    public static List<FocusStop> tabThroughPage(WebDriver driver, int presses) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Put the Tab starting point back at the top of the document, the
        // same place a user starts from when a page first loads.
        js.executeScript(
                "window.scrollTo(0, 0);" +
                "if (document.activeElement) document.activeElement.blur();" +
                "document.body.setAttribute('tabindex', '-1');" +
                "document.body.focus();" +
                "document.body.removeAttribute('tabindex');");

        Actions keyboard = new Actions(driver);
        List<FocusStop> stops = new ArrayList<>();
        for (int press = 1; press <= presses; press++) {
            keyboard.sendKeys(Keys.TAB).perform();
            Object raw = js.executeScript(FOCUS_INFO_JS);
            if (!(raw instanceof Map<?, ?> info)) {
                stops.add(new FocusStop(press, null, "(focus stayed on the page body)", false, false, false, false));
                continue;
            }
            // Square brackets, NOT angle brackets: the Cucumber HTML report
            // renders scenario.log() text as HTML, so "<input>" would be drawn
            // as a real text box and "<a>" would swallow the label.
            String role = String.valueOf(info.get("role"));
            String label = String.valueOf(info.get("label"));
            boolean hasAccessibleName = !label.isBlank();
            String description = "[" + info.get("tag") + "]"
                    + (role.isBlank() ? "" : " role=" + role)
                    + (hasAccessibleName ? " \"" + label + "\"" : " (no accessible name)");
            stops.add(new FocusStop(press,
                    (WebElement) info.get("el"),
                    description,
                    Boolean.TRUE.equals(info.get("interactive")),
                    Boolean.TRUE.equals(info.get("visible")),
                    Boolean.TRUE.equals(info.get("focusIndicator")),
                    hasAccessibleName));
        }
        return stops;
    }

    // Results has no built-in toJSON() in this axe-core-selenium version,
    // so it's serialized with the Jackson ObjectMapper that ships as one of
    // axe-core's own transitive dependencies (no extra dependency needed).
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static void writeReport(Results results, String pageLabel) {
        try {
            Files.createDirectories(REPORT_DIR);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeLabel = pageLabel.replaceAll("[^a-zA-Z0-9-]", "_");
            Path file = REPORT_DIR.resolve("axe_" + safeLabel + "_" + timestamp + ".json");
            JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), results);
            log.info("Accessibility report written to {}", file.toAbsolutePath());
        } catch (IOException e) {
            log.error("Could not write accessibility report for {}", pageLabel, e);
        }
    }
}
