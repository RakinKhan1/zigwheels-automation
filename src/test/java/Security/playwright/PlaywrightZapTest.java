package security.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Proxy;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import security.config.SecurityConfig;
import security.zap.ZapSecurityScanner;

import static security.config.SecurityConfig.ZAP_PROXY;

public class PlaywrightZapTest {

    private static final ZapSecurityScanner zap = new ZapSecurityScanner();

    @BeforeClass
    public void checkZap() throws Exception {
        zap.checkConnection();
    }

    @Test
    public void browseTargetsThroughZap() {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setProxy(new Proxy(ZAP_PROXY))
            );

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)
            );

            Page page = context.newPage();

            for (String target : SecurityConfig.TARGETS) {
                page.navigate(target, new Page.NavigateOptions()
                        .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(60000));
                page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);

                String title = page.title();
                String bodyText = page.locator("body").innerText();

                Assert.assertFalse(title.isBlank(),
                        "Expected a page title for " + target);
                Assert.assertFalse(bodyText.isBlank(),
                        "Expected visible content for " + target);

                System.out.println("Visited through ZAP: " + target + " | " + title);
            }

            browser.close();
        }
    }

    @AfterClass(alwaysRun = true)
    public void collectPassiveResults() throws Exception {
        zap.waitForPassiveScanner();

        for (String target : SecurityConfig.TARGETS) {
            zap.printAlertSummary(target);
            zap.enforceQualityGate(target);
        }
    }
}