package stepdefinitions;

import com.deque.html.axecore.results.Rule;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import utils.AccessibilityUtils;
import utils.AccessibilityUtils.FocusStop;
import utils.DriverFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Accessibility steps shared by every feature (Key Automation Scope:
 * "basic accessibility test like Screen reader compatibility, Keyboard
 * only accessibility").
 *
 * Two halves:
 *   1. Screen reader compatibility: an axe-core scan for the issues that
 *      break screen readers (missing link names, missing alt text, missing
 *      page language, unlabelled form controls, ARIA misuse).
 *   2. Keyboard only: presses the real Tab key through the page and checks
 *      where focus lands.
 *
 * Findings are written into the Cucumber/Extent report for the scenario
 * (scenario.log), not only to the console, so they are visible in the demo.
 */
public class AccessibilitySteps {

    private static final Logger log = LoggerFactory.getLogger(AccessibilitySteps.class);

    /** How many Tab presses the keyboard check makes from the top of the page. */
    private static final int TAB_PRESSES = 15;

    /** Minimum number of different controls focus must reach to pass. */
    private static final int MIN_DISTINCT_STOPS = 3;

    /**
     * Runs the axe-core scan and REPORTS what it finds. The step wording says
     * "report" on purpose: it does not fail the scenario because zigwheels.com
     * has its own genuine violations (a third-party site this project does not
     * own). Failing the build on them would make every run red for reasons no
     * one on this project can fix. The scan still runs for real, the counts
     * go into the report, and the full detail is saved as JSON in
     * target/accessibility-reports/.
     */
    @Then("I scan the page with axe-core and report any critical or serious accessibility violations")
    public void i_scan_the_page_and_report_accessibility_violations() {
        List<Rule> violations = AccessibilityUtils.scanPage(DriverFactory.getDriver(), currentPageLabel());
        Assert.assertNotNull(violations, "axe-core scan did not return a result");

        List<Rule> criticalOrSerious = violations.stream()
                .filter(v -> "critical".equals(v.getImpact()) || "serious".equals(v.getImpact()))
                .toList();

        StringBuilder summary = new StringBuilder("axe-core scan of ")
                .append(DriverFactory.getDriver().getCurrentUrl()).append('\n')
                .append("Violation types found: ").append(violations.size())
                .append(" (critical/serious: ").append(criticalOrSerious.size()).append(")\n");
        for (Rule v : violations) {
            summary.append("  [").append(v.getImpact()).append("] ").append(v.getId())
                    .append(" - ").append(v.getNodes().size()).append(" element(s)\n");
        }
        writeToReport(summary.toString());

        if (!criticalOrSerious.isEmpty()) {
            log.warn("FINDING: {} critical/serious accessibility violation type(s) on zigwheels.com's live page - "
                    + "see target/accessibility-reports/ for the full axe-core detail. Reported, not failed, "
                    + "because this is a third-party site's own accessibility gap.", criticalOrSerious.size());
        }
    }

    /**
     * Real keyboard-only check: presses Tab from the top of the page and
     * records every element focus lands on.
     *
     * FAILS the scenario if a keyboard-only user cannot move through the page
     * at all (fewer than MIN_DISTINCT_STOPS different visible controls reached),
     * because that would mean the page is unusable without a mouse.
     *
     * REPORTS, without failing: focus landing on non-interactive or hidden
     * elements, and controls with no visible focus indicator. These are real
     * accessibility issues on zigwheels.com, not defects in this test suite.
     */
    @And("a keyboard-only user can Tab through the first interactive elements on the page")
    public void a_keyboard_only_user_can_tab_through_the_page() {
        List<FocusStop> stops = AccessibilityUtils.tabThroughPage(DriverFactory.getDriver(), TAB_PRESSES);

        List<WebElement> distinctVisible = new ArrayList<>();
        int stuckOnBody = 0;
        int notInteractive = 0;
        int hidden = 0;
        int noFocusIndicator = 0;
        int noAccessibleName = 0;

        StringBuilder journey = new StringBuilder("Keyboard Tab journey (" + TAB_PRESSES + " presses):\n");
        for (FocusStop stop : stops) {
            journey.append("  Tab ").append(stop.press()).append(": ").append(stop.description());
            if (stop.element() == null) {
                stuckOnBody++;
                journey.append('\n');
                continue;
            }
            if (!stop.hasAccessibleName()) {
                noAccessibleName++;
            }
            if (!stop.interactive()) {
                notInteractive++;
                journey.append("  [not an interactive control]");
            }
            if (!stop.visible()) {
                hidden++;
                journey.append("  [not visible on screen]");
            } else if (!distinctVisible.contains(stop.element())) {
                distinctVisible.add(stop.element());
            }
            if (stop.visible() && !stop.focusIndicator()) {
                noFocusIndicator++;
                journey.append("  [no visible focus indicator]");
            }
            journey.append('\n');
        }
        journey.append("Different visible controls reached: ").append(distinctVisible.size()).append('\n')
                .append("Findings: ").append(notInteractive).append(" non-interactive stop(s), ")
                .append(hidden).append(" hidden stop(s), ")
                .append(noFocusIndicator).append(" stop(s) with no visible focus indicator, ")
                .append(noAccessibleName).append(" control(s) with no accessible name (a screen reader has nothing to announce), ")
                .append(stuckOnBody).append(" press(es) where focus did not move");

        writeToReport(journey.toString());

        Assert.assertTrue(distinctVisible.size() >= MIN_DISTINCT_STOPS,
                "Keyboard-only navigation failed: Tab reached only " + distinctVisible.size()
                        + " different visible control(s) in " + TAB_PRESSES + " presses (needed at least "
                        + MIN_DISTINCT_STOPS + "). Journey:\n" + journey);
    }

    private void writeToReport(String text) {
        log.info("\n{}", text);
        Scenario scenario = Hooks.currentScenario();
        if (scenario != null) {
            scenario.log(text);
        }
    }

    private String currentPageLabel() {
        String url = DriverFactory.getDriver().getCurrentUrl();
        return url.replaceAll("https?://", "").replaceAll("[/?].*", "_")
                + url.replaceAll(".*(zigwheels\\.com|google\\.com)", "").replaceAll("[^a-zA-Z0-9]+", "_");
    }
}
