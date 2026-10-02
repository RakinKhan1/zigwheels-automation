# Demo script: Teams call with BU stakeholders

Case Study 2, "Identify New Bikes". About 12 minutes plus questions.
Lines in quotes are things to say (in your own words is fine). Everything
else is what to do on screen.

---

## Before the call (15 minutes before)

1. Open IntelliJ on `Downloads\zigwheels-automation-v2\zigwheels-automation`.
2. In the Git Bash terminal, run `mvn clean test` once as a warm-up. It
   takes about a minute. Check it ends with **Tests run: 6, Failures: 0**
   and **BUILD SUCCESS**. This also gives you a fresh report as a backup.
3. Open these in Chrome tabs, in this order, so you can click straight
   between them:
   - The brief (the "Identify New Bikes" screenshot)
   - `target/cucumber-reports/cucumber.html` (right-click in IntelliJ,
     Open In, Browser, Chrome)
   - `target/extent-report/ExtentReport.html` (same way)
   - The Allure report: in a **second** terminal tab, run
     `mvn allure:serve` and leave it running. It opens in Chrome by itself.
   - `evidence/jmeter/report/index.html` (the JMeter dashboard)
   - `evidence/zap/zigwheels-passive-scan.html` (the ZAP report)
4. Close Outlook, Teams chat pop-ups and anything else that could appear
   on a shared screen.
5. When you share, share the **whole screen**, not one window, so the
   Chrome windows that open during the live run are visible.

---

## 1. Opening (30 seconds)

Show the brief.

> "This is case study 2 from the QEA training. The brief has three
> scenarios on zigwheels.com: find upcoming Honda bikes under 4 lakh,
> list the popular used car models in Chennai, and try a Google login with
> invalid details and capture the error. On top of that there's a list of
> automation scope items, from windows and frames through to BDD,
> parallel runs, performance and accessibility. I built it as one Java
> project using Selenium, Cucumber and TestNG, so everything from the
> eight weeks sits in one stack."

---

## 2. How it's built (1 to 2 minutes)

In IntelliJ, expand `src/test` in the Project panel.

> "It follows the Page Object Model. Each page on the site has its own
> class in `pages`, so locators live in one place and the step
> definitions never touch them directly."

Open `UpcomingHondaBikes.feature`.

> "The scenarios are written in Gherkin, so they read almost word for word
> like the brief. Anyone on the business side can read what's being tested
> without reading Java."

Open `testng.xml`, then `DriverFactory.java`.

> "Scenarios run in parallel on three threads. Each thread gets its own
> browser through a ThreadLocal, so they never interfere with each other."

---

## 3. Live run (about 1 minute of runtime)

In the terminal, run:

```bash
mvn clean test
```

While the Chrome windows open and close:

> "You'll see several browsers open at once. That's the parallel
> execution. In one of them you'll see zigwheels open its login box and a
> separate Google sign-in window pop up. That's the windows handling: the
> test switches into that popup, tries the invalid login, closes it and
> switches back to zigwheels."

When it finishes, point at **Tests run: 6, Failures: 0, BUILD SUCCESS**.

**If something fails live:** don't debug on the call. Say "that's the live
site responding differently this time, which is exactly why I keep the
last good report", then switch to the report tab you opened beforehand.
Google sometimes slows down automated sign-ins, which is the most likely
cause.

---

## 4. The three scenarios in the report (3 minutes)

Switch to the Cucumber report tab and refresh it (F5).

**Honda bikes.** Expand `UpcomingHondaBikes.feature`.

> "It first pulls the top menu into a list, which covers the 'extract menu
> items into collections' item. Then it goes to upcoming bikes, filters to
> Honda under 4 lakh, and checks that every bike has a name, a price and a
> launch date. At the end it navigates back to the home page and checks it
> really got there."

Read out a couple of the bikes it found from the log.

**Chennai used cars.** Expand `UsedCarsChennai.feature`.

> "This reads the Popular Models list on the Chennai page. There are eight
> models. Underneath it also shows the Top 10 used cars in Chennai with
> listing counts, as a bonus."

**Google login.** Expand `GoogleInvalidLogin.feature`.

> "This starts on zigwheels, not on Google. It opens the login box, clicks
> Google, switches into the popup window, enters an account that doesn't
> exist, and captures Google's message, 'Couldn't find this account'.
> Then it closes the popup and confirms it's back on zigwheels."

**Frames.** Expand `WindowsAndFrames.feature`.

> "The zigwheels home page loads its own iframes, mainly Google's consent
> and advertising frames. The test lists all of them into a collection,
> switches into one, proves it's inside, and switches back out."

---

**Other reports (30 seconds).** Switch to the Extent tab, then the Allure tab.

> "The same run also produces an Extent report and an Allure report. Extent
> gives a dashboard view with each scenario's steps, logs and a screenshot.
> Allure is the one teams usually plug into a CI pipeline: it shows pass
> rates, timings per step, and it keeps history across runs."

## 5. Accessibility (1 to 2 minutes)

In the report, open **Basic accessibility checks on the used cars Chennai
page**.

> "The brief asks for screen reader compatibility and keyboard-only
> access, so there are two checks."

Point at the axe-core box.

> "The first is an axe-core scan, which looks for things that break screen
> readers. On the Chennai page it found seven types of issue, six of them
> critical or serious: links with no name, a missing page language,
> unlabelled dropdowns and low colour contrast."

Point at the Keyboard Tab journey box.

> "The second actually presses the Tab key fifteen times, like a keyboard
> user would, and records where focus lands. Twelve of the fourteen
> controls it reached had no visible focus outline, so a keyboard user
> can't see where they are. It also found two controls with no name, and
> axe-core flagged the same problem independently."

> "These are reported, not failed, because they're zigwheels' own issues
> and not something this project can fix. The keyboard test does fail if a
> user can't move through the page at all."

---

## 6. Performance (1 minute)

First, in the report, point at the "Page load performance" box in the Honda
bikes scenario.

> "Every test run records how long each page took to load, straight from
> the browser: server response time, when the page became usable, and full
> load. That runs automatically with the functional tests."

Then switch to the JMeter dashboard tab.

> "For load, which one browser can't generate, I used JMeter."

> "For performance I used JMeter against the upcoming bikes page: 50
> virtual users, 250 requests in total. Error rate was zero. Average
> response time was 447 milliseconds and throughput was about 20 requests
> a second."

> "One thing worth pointing out: the slowest response was about 1.1
> seconds, roughly two and a half times the average, and that pattern
> repeated across two runs. So most requests are fast, with a small number
> of slow outliers. That's the kind of thing I'd raise with a team rather
> than hide."

---

## 7. Security, as a bonus (30 seconds)

Switch to the ZAP report tab.

> "Security wasn't in the brief, but I'd already run an OWASP ZAP passive
> scan on zigwheels in week 7, so I included it. No high-risk findings;
> nine medium, mostly Content Security Policy settings, and ten low."

---

## 8. What I learned (1 minute): say this unprompted

> "The biggest lesson was that passing tests and tested-against-the-real-
> site aren't the same thing. A few examples:"

- > "The brief's own suggested pattern for extracting bike names doesn't
  > match the real page. I checked the actual HTML and built the locators
  > from that."
- > "The Popular Models list sits inside its own small scrolling box.
  > Selenium doesn't return text for items scrolled out of view, so my
  > first version quietly read a different table. I found it by inspecting
  > the live page, fixed it, and removed the fallback so a missing list now
  > fails the test instead of hiding the problem."
- > "Google's error message uses a curly apostrophe, not a straight one,
  > which silently broke the text match. I found that from a failure
  > screenshot."

Close with:

> "Everything in the brief is covered and it all runs against the live
> site. Happy to take any questions."

---

## Likely questions

**"Why don't the accessibility violations fail the build?"**
They're zigwheels' own issues. If a build fails for things nobody on the
project can fix, people learn to ignore red builds. The findings are still
recorded in the report and saved as JSON. The keyboard test does fail if
the page can't be used without a mouse.

**"Why are the performance results from JMeter and not in this suite?"**
Load testing and UI testing are different jobs. Selenium drives one real
browser per scenario; JMeter simulates many users cheaply. The JMeter test
plans and raw results are in the `evidence` folder so the numbers can be
checked.

**"Why a fake Google account?"**
The brief asks for an invalid login, and using real credentials in test
code would be a security risk. The test only needs Google to reject the
account.

**"What happens if zigwheels changes its page?"**
Locators are in the page classes only, so a change means updating one
file. The used cars test was also changed to fail loudly if the list
disappears, rather than quietly using other data.

**"Could this run in a CI pipeline?"**
Yes. Set `headless=true` in `config.properties` and run `mvn clean test`.
The Cucumber report and accessibility JSON files come out as build
artefacts.

**"Could it run for another city?"**
Yes. The city comes from the feature file, so changing "Chennai" to
another city is a one-word change.

**Running one scenario on its own** (useful if asked to rerun something):

```bash
mvn test -Dcucumber.filter.tags="@bikes"
mvn test -Dcucumber.filter.tags="@usedcars"
mvn test -Dcucumber.filter.tags="@googlelogin"
mvn test -Dcucumber.filter.tags="@frames"
mvn test -Dcucumber.filter.tags="@accessibility"
```
