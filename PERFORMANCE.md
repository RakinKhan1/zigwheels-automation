# Performance testing: response time, throughput, error rate

The brief asks to "capture some performance metrics like Response time,
Throughput, Error rate". This project covers that in two layers:

| Layer | Tool | What it measures | When it runs |
|---|---|---|---|
| Load test | JMeter | Response time, throughput and error rate under 50 concurrent users | On demand (see "Re-running the JMeter test" below) |
| Page load timings | Selenium + the browser's Navigation Timing API | Server response time, time until the page is usable, full load time, for one real browser | Every `mvn clean test` run, in the `@performance` scenarios |

A single Selenium browser cannot generate load, so throughput and error
rate come from JMeter. The page load timings add a response-time check that
runs automatically with the functional tests.

## Layer 1: JMeter load test

Rather than duplicate this as a fresh exercise, this project folds in the
JMeter performance test already built and run against **the exact same
target page** this Selenium suite automates (`zigwheels.com/upcoming-bikes`).
That work happened earlier in this training and is genuinely complete:
the numbers below are from a real, clean full run, not projected.

## Test design

- **Test Plan**: `evidence/jmeter/zigwheels_combined.jmx` (also present as two standalone
  files: `View Results Tree.jmx` for the hand-built HTTP request plan,
  and `zigwheels_recorder_practice.jmx` for the separate HTTP(S) Test
  Script Recorder exercise)
- **Thread Group**: 50 users, 10s ramp-up, 5 loops (250 total requests)
- **Target**: `GET https://www.zigwheels.com/upcoming-bikes`
- **Extractors**: 3 Regular Expression Extractors pulling bike name,
  price and expected launch date straight off the real page HTML
  (see README's note on the brief's own suggested regex not matching the
  real markup; the working regex anchors on `data-track-label="model-name"`
  and the `Ex-Showroom Price` / `Expected Launch` div text instead)
- **Listeners**: View Results in Table, Summary Report, Response Time Graph

## Results: full 250-request run, 0 failures

| Metric | Value |
|---|---|
| Total requests | 250 (50 users × 5 loops) |
| Error rate | **0.00%** |
| Average response time | 447 ms |
| Min | 165 ms |
| Max | 1,070 ms |
| Std. Dev. | 280.68 |
| Throughput | 19.9 requests/sec |
| Received | 7,474.71 KB/sec |

**Observation worth quoting in the demo**: Max (1,070 ms) is roughly 2.4×
the Average across two separate full runs on this page: a consistent,
repeatable pattern of a small number of slower outlier requests rather
than uniformly slow responses under load. Not chased further since it
falls outside this project's scope, but it's the kind of signal a real
performance baseline is supposed to surface.

## HTML report

Generated via `jmeter -g result.jtl -o report`, a full JMeter Dashboard
(`evidence/jmeter/report/index.html`) with the Requests Summary pass/fail pie chart and
Apdex scoring (0.747 overall, "Fair" per standard T=500ms/F=1.5s thresholds).

## Also covered: HTTP(S) Test Script Recorder

A separate, deliberately isolated Test Plan
(`zigwheels_recorder_practice.jmx`) walks through JMeter's record-and-replay
workflow against the same site: Recording Controller, certificate install,
browser proxy configuration, and a clean replay via View Results Tree,
demonstrating the recorder-based approach to script creation alongside the
hand-built HTTP Request approach used for the metrics above.

## Layer 2: page load timings in every test run

The bikes and used cars scenarios include the step
`I capture the page load performance metrics for the "..." page`
(`PerformanceSteps` / `utils/PerformanceUtils`). It reads the browser's own
Navigation Timing data for the page just loaded and records:

- **Server response time**: request sent to first byte received. Compared
  against a 1,000 ms budget and reported as within or over it.
- **Page usable (DOM ready)**: when the HTML was parsed and the page could be
  used.
- **Fully loaded**: when the page and all its resources finished loading.
  zigwheels keeps loading adverts for a while, so this can show "still
  loading" after 10 seconds.

Results appear in the Cucumber, Extent and Allure reports for each run and
are appended to `target/performance/page-load-timings.csv`. The step only
fails if the browser returns no timing data at all; slow pages are reported,
not failed, because this is a third-party live site.

## Re-running the JMeter test

From the project folder, in Git Bash. JMeter is started with `java -jar`
rather than `jmeter.bat`, because the batch launcher fails when its own path
contains a space (as in `C:\Users\450 G10\...`):

```bash
java -jar "/c/Users/450 G10/Downloads/apache-jmeter-5.6.3/apache-jmeter-5.6.3/bin/ApacheJMeter.jar" -n -t "evidence/jmeter/View Results Tree.jmx" -l evidence/jmeter/rerun/result.jtl -e -o evidence/jmeter/rerun/report
```

`-n` runs without the GUI, `-t` is the test plan, `-l` saves the raw results,
and `-e -o` builds a fresh HTML dashboard. The output folder must not already
exist, so delete or rename `evidence/jmeter/rerun` before running it again.
`View Results Tree.jmx` is the hand-built plan the results above come from.
`zigwheels_combined.jmx` also contains the recorder replay (dozens of
analytics and tracking requests), so it is not used for the headline numbers.

## Re-run results, 23 Sep 2026 (command line)

Same plan (50 users, 10 s ramp-up, 5 loops) against the same page.
Dashboard: `evidence/jmeter/rerun/report/index.html`.

| Metric | 10 Sep 2026 (GUI) | 23 Sep 2026 (command line) |
|---|---|---|
| Requests | 250 | 250 |
| Error rate | 0.00% | **0.00%** |
| Average response time | 447 ms | 3,555 ms |
| Min / Max | 165 ms / 1,070 ms | 364 ms / 20,265 ms |
| Throughput | 19.9 requests/sec | 7.2 requests/sec |

**Observation.** The site served all 250 requests without a single error
on both days, but responses were much slower on the re-run. Each page is
about 380 KB, so 7.2 requests/sec is roughly 2.7 MB/s arriving at the test
machine, against about 7.5 MB/s on 10 Sep. That points to the test
machine's network connection as a likely bottleneck on 23 Sep, rather than
the site itself; the single-browser timings captured by the test suite on
the same day showed a server response of about 0.6 seconds. This has not
been proven either way. It is recorded because it illustrates a real limit
of load testing from one laptop: the result measures the site and the
network path together. A fair comparison would run both tests from the
same network, or from a machine in a data centre.
