# ZigWheels Automation — Cognizant QEA Final Project (Case Study 2)

Automates the three "Identify New Bikes" hackathon scenarios end to end,
using the full toolset covered across the 8-week Cognizant QEA training:
Selenium, Cucumber BDD, TestNG parallel execution, Page Object Model,
axe-core accessibility checks, JMeter performance testing, and an OWASP
ZAP security scan.

## The three scenarios

| # | Scenario | Brief requirement | Where it lives |
|---|----------|--------------------|-----------------|
| 1 | Upcoming Honda bikes | Name, price, expected launch date; manufacturer = Honda; price < 4 Lakh | `UpcomingHondaBikes.feature` / `UpcomingBikesPage` |
| 2 | Chennai used cars | Extract all popular models into a List; display it | `UsedCarsChennai.feature` / `UsedCarsChennaiPage` |
| 3 | Invalid Google login | Start from zigwheels' Login box, choose Google, switch into the Google popup window, attempt login with bad credentials, capture the error message, close the popup and switch back | `GoogleInvalidLogin.feature` / `HomePage` + `GoogleLoginPage` |

Plus a **frame handling** feature (`WindowsAndFrames.feature` / `HomePage`):
it lists zigwheels' own iframes into a `List<String>`, switches into one,
proves the driver is inside it, and switches back. Window handling runs for
real inside scenario 3, because zigwheels opens Google's sign-in in a
separate popup window. Menu-item extraction into a `List<String>` runs as
part of scenario 1, on the home page before it navigates to the bikes page.

## Key Automation Scope — mapped honestly

The brief lists eleven capabilities to demonstrate. Here's exactly where
each one is real, and where a capability genuinely wasn't exercised
because none of the three scenarios needed it (rather than claiming
coverage that isn't there):

| Scope item | Status | Where |
|---|---|---|
| Handling windows & frames | ✅ Real | **Windows:** `GoogleInvalidLogin.feature`. zigwheels' Login box opens Google's sign-in in a separate popup window (confirmed live 23 Sep 2026); the test switches into it, then closes it and switches back. **Frames:** `WindowsAndFrames.feature` switches into one of the zigwheels home page's own iframes (13 found live on 23 Sep 2026) and back |
| Filling simple form, capture warning message | ✅ Real | `GoogleLoginPage` — fills email/password, captures whichever real Google error text appears |
| Extract menu items from frames & store in collections | ✅ Real | `HomePage.extractTopNavMenuItems()` stores the real top-nav labels in a `List<String>` (scenario 1). zigwheels' menu is plain page content, not inside a frame, so the frames feature separately stores the page's iframes in a `List<String>` via `HomePage.extractIframesIntoList()` |
| Navigating back to home page | ✅ Real | Last two steps of the bikes and used-cars scenarios: `I navigate back to the home page` calls `BasePage.navigateToHomePage()`, then `I should be back on the zigwheels home page` asserts the URL and that the home page's nav bar has loaded |
| BDD based automation | ✅ Real | 4 Gherkin feature files, Cucumber-Java step definitions |
| Implement parallel test execution | ✅ Real | `TestRunner` overrides `scenarios()` with `@DataProvider(parallel=true)`, `testng.xml` sets `data-provider-thread-count="3"`, `DriverFactory` uses `ThreadLocal<WebDriver>` so parallel threads never share a browser |
| Follow Page Object Model | ✅ Real | `pages/` package — one class per page, `BasePage` for shared plumbing |
| Capture performance metrics (response time, throughput, error rate) | ✅ Real | Two layers, see [PERFORMANCE.md](PERFORMANCE.md): a JMeter load test on the upcoming bikes page (throughput, error rate, response time under 50 users; files in `evidence/jmeter`), plus page load timings captured by the browser in every test run (`@performance` scenarios, saved to `target/performance/page-load-timings.csv`) |
| Basic accessibility test (screen reader, keyboard-only) | ✅ Real | `AccessibilityUtils` — axe-core scan (screen-reader-relevant: alt text, ARIA, contrast, labels) + a real keyboard-only check that presses the Tab key 15 times from the top of the page and records every element focus lands on (fails if keyboard users can't reach at least 3 visible controls; reports hidden/non-interactive stops, controls with no accessible name, and missing focus outlines). Both halves write their findings into the Cucumber report |

## Project structure

```
zigwheels-automation/
├── pom.xml
├── testng.xml
├── src/test/java/
│   ├── pages/            Page Object Model (BasePage + one class per page)
│   ├── stepdefinitions/  Cucumber step definitions + Hooks (driver lifecycle, screenshots on failure)
│   ├── runners/          TestRunner (Cucumber-on-TestNG, parallel)
│   └── utils/            ConfigReader, DriverFactory (thread-safe), AccessibilityUtils
└── src/test/resources/
    ├── config.properties
    └── features/         4 .feature files — one per business scenario,
                           plus the windows & frames capability check
```

## Live verification status (done)

This project's Selenium locators were originally built without live
browser access, so this checklist used to say "do this before the demo."
That pass has since actually happened, live, on 21 Sep 2026 — the same
DevTools-first habit as the JMeter work, applied here:

1. **Honda bikes page** — confirmed (reused the verified JMeter
   selectors: `data-price`, `data-explaunch`, the
   `title`+`data-track-label="model-name"` pattern). No issues found.
2. **Chennai used cars page** - first live run (21 Sep 2026) found
   nothing with the original "Popular Models" locator, so the code fell
   back to the "Top 10 Used Cars In Chennai" table. Rechecked in DevTools
   on 23 Sep 2026: the list is there, under "Brand and Model", as
   `<ul class="... popularModels ...">` with one `<label>` per model
   (8 for Chennai). It sits in its own small scrolling box, and Selenium's
   `getText()` returns nothing for items scrolled out of view, which is
   why the old version found no text. Fixed by targeting
   `ul.popularModels li label` and reading each label's `textContent`.
   The silent fallback was removed, so the scenario now fails if the list
   is ever missing, and the city passed in from the feature file is now
   actually used to build the URL.
3. **Google login page** — run live with a saved failure screenshot as
   evidence. Two real findings, both fixed in code:
   - The actual wording is "Couldn't find **this** account", not
     "...your Google Account" — and Google's apostrophe is a Unicode
     curly quote (’), not a plain ASCII one (') — a real character-encoding
     mismatch that silently broke the original XPath match. Fixed by
     matching on "find this account" (no apostrophe in the match at all).
   - The error takes longer than 8 seconds to appear (Google visibly
     throttles automated ChromeDriver sessions) — the wait was extended
     to 25 seconds.
4. **Windows & frames capability check + menu extraction** — run live on
   22 Sep 2026 in IntelliJ on Rakin's own machine (the first real run
   outside this session). Two real findings, both fixed:
   - The new-tab switch failed because Chrome's default popup blocker
     silently swallowed the `window.open()` call, so no second window
     handle ever appeared. Fixed by adding `--disable-popup-blocking` to
     `DriverFactory`'s ChromeOptions.
   - `extractTopNavMenuItems()` returned an empty list because zigwheels'
     top-nav labels are `<span class="c-p ...">` elements for dropdown
     items, not `<a>` tags — confirmed via DevTools inspection of the
     real `<nav class="headerNav"><ul class="h-d-nav">` markup. Fixed by
     anchoring on the `<li>` structure and reading whichever direct-child
     element (`<a>` or `<span>`) actually holds the label.
   - Even with that fix, extraction still came back empty. Root cause
     (confirmed via the saved failure screenshot): zigwheels' GDPR
     cookie-consent dialog - a different popup from the city-selection
     one already handled - covers the whole page on every automated run,
     since a fresh Chrome profile has no consent cookie saved (unlike a
     normal browser session, which only sees it once). Fixed by adding
     `HomePage.dismissCookieConsentIfPresent()`, run before the
     city-modal check.

5. **Login flow, windows and frames** - checked live via DevTools on
   23 Sep 2026. The earlier version of this project said zigwheels never
   opens a second window or has a real iframe; that turned out to be
   wrong once the site was actually inspected:
   - zigwheels' person icon (`div.zw-icon-my-account`) opens a
     "Login/Register" box whose **Google** button (`div.googleSignIn`)
     opens Google's sign-in in a **separate popup window**. Scenario 3 now
     starts from zigwheels and switches into that popup, instead of going
     straight to Google's page.
   - The home page contains **13 real iframes** (Google consent and ad
     frames). The frames feature now switches into one of those, instead
     of an iframe the test created itself.

Net result: 3 of 4 feature files needed a real fix that only a live run
would have caught — the strongest evidence in this whole project that
"tests pass" and "actually tested against the real site" are two
different things, and this project did the second one.

## Running it

```bash
mvn clean test
```

This runs all scenarios in parallel (3 threads), generates:
- `target/cucumber-reports/cucumber.html` — BDD run report
- `target/extent-report/ExtentReport.html` — Extent report (dashboard, per-scenario steps, logs and screenshots), configured in `src/test/resources/extent.properties`
- `target/performance/page-load-timings.csv` — page load timings from the `@performance` scenarios
- `target/allure-results/` — Allure results. To view the Allure report, run `mvn allure:serve` after the tests (it builds the report and opens it in the browser; the first time, Maven downloads the Allure tool)
- `target/accessibility-reports/*.json` — one axe-core report per page scanned
- `target/surefire-reports/` — TestNG results

To run a single scenario by tag:
```bash
mvn test -Dcucumber.filter.tags="@bikes"
mvn test -Dcucumber.filter.tags="@usedcars"
mvn test -Dcucumber.filter.tags="@googlelogin"
```

## Performance, accessibility & security deliverables

- **Performance**: [PERFORMANCE.md](PERFORMANCE.md) — full JMeter results
  (250 requests, 0% error rate, response time/throughput/error rate all
  captured) against the same upcoming-bikes page this Selenium suite tests.
- **Accessibility**: axe-core runs automatically as part of the `@accessibility`
  tagged scenarios in each feature file; reports land in
  `target/accessibility-reports/`, and the findings (axe-core violations and
  the keyboard Tab journey) are also written into the Cucumber HTML report.
- **Evidence**: the JMeter test plans, raw results and HTML dashboard, and the
  ZAP scan report, are kept in [evidence/](evidence/) so the numbers in
  PERFORMANCE.md and SECURITY.md can be checked.
- **Security**: [SECURITY.md](SECURITY.md) — not in this project's scope,
  but folds in the OWASP ZAP passive scan already run against zigwheels.com
  from the Week 7 OWASP module (0 High, 9 Medium, 10 Low, 8 Informational).

## Demo notes

See [DEMO_NOTES.md](DEMO_NOTES.md) for a suggested run-through structure
for the Teams call with BU stakeholders.
