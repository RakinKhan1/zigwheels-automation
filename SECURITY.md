# Security testing

Not part of this project's Key Automation Scope, and not duplicated here.
Same approach as `PERFORMANCE.md`: this folds in the OWASP ZAP passive scan
already run against **the same target site** this Selenium suite
automates (zigwheels.com), from the earlier Week 7 OWASP/ZAP module. The
numbers below are from a real, saved scan report, not projected.

## Test design

- **Tool**: OWASP ZAP 2.17.0
- **Scan type**: Passive scan only (no active/attack scanning against the
  live site — consistent with the Week 7 lab's scope, which limited active
  scanning to a local OWASP Juice Shop instance and used passive-only
  scanning against real third-party sites)
- **Target**: zigwheels.com (crawled traffic also touched a wide set of
  third-party domains the site loads — ad/analytics/CDN providers such as
  Google, Facebook, Cardekho, Bikedekho, Connecto and others — captured in
  the report's site list since ZAP passively observes everything the
  browser loads, not just the first-party domain)
- **Report**: `evidence/zap/zigwheels-passive-scan.html`, generated 17 Sep 2026

## Results

| Risk Level | Number of Alerts |
|---|---|
| High | 0 |
| Medium | 9 |
| Low | 10 |
| Informational | 8 |
| False Positives | 0 |

No High-risk findings. The Medium and Low findings are mostly standard,
low-severity header/cookie hardening gaps rather than exploitable
vulnerabilities.

### Notable findings

**Medium:**
- **CSP: Failure to Define Directive with No Fallback** — the site's
  Content-Security-Policy sets `frame-ancestors` but leaves `form-action`
  undefined with no fallback to `default-src`, which is functionally the
  same as allowing form submissions anywhere. Flagged across multiple
  pages (homepage, review-count endpoint, geo-lookup endpoint, sponsored
  content pages).
- **Content Security Policy (CSP) Header Not Set** / **CSP: Wildcard
  Directive** / **CSP: script-src unsafe-eval** / **unsafe-inline** (script
  and style) — a cluster of CSP configuration gaps, the most common
  category of Medium finding in this scan.
- **Session ID in URL Rewrite** — session identifiers appearing in URLs
  rather than only in cookies, which risks leaking session data via
  browser history, referrer headers or server logs.
- **Sub Resource Integrity Attribute Missing** — externally-loaded
  scripts/styles without SRI hashes, so a compromised third-party CDN
  could serve modified content undetected.
- **Cross-Domain Misconfiguration**.

**Low:**
- **Server Leaks Version Information via "Server" HTTP Response Header
  Field** (11 instances) — most frequent Low finding.
- Cookie hardening gaps: **Cookie Without Secure Flag**, **Cookie No
  HttpOnly Flag**, **Cookie with SameSite Attribute None** / **without
  SameSite Attribute**.
- **Strict-Transport-Security Header Not Set**, **X-Content-Type-Options
  Header Missing**, **Timestamp Disclosure - Unix**.

**Informational:** mostly cache-control and session-management
observations, plus **Information Disclosure - Suspicious Comments** (24
instances) and **Information Disclosure - Sensitive Information in URL**
(4 instances) worth a manual look even though ZAP doesn't rate them as a
risk on their own.

## Scope note

These are real findings on zigwheels.com's live, third-party site, not a
defect in this test suite — the same posture taken with the accessibility
scan in this project (see `AccessibilitySteps.java`). Full per-alert detail
(affected URLs, evidence, and ZAP's remediation guidance for each) is in
the saved HTML report referenced above.
