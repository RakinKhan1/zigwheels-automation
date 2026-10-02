package security.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import security.config.SecurityConfig;
import security.zap.ZapSecurityScanner;

import static security.config.SecurityConfig.ZAP_HOST;
import static security.config.SecurityConfig.ZAP_PORT;

public class SeleniumZapTest {

    private static final ZapSecurityScanner zap = new ZapSecurityScanner();

    @BeforeClass
    public void checkZap() throws Exception {
        zap.checkConnection();
    }

    @Test
    public void browseTargetsThroughZap() {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(ZAP_HOST + ":" + ZAP_PORT);
        proxy.setSslProxy(ZAP_HOST + ":" + ZAP_PORT);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);
        options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);

        try {
            for (String target : SecurityConfig.TARGETS) {
                driver.get(target);

                String title = driver.getTitle();
                String bodyText = driver.findElement(By.tagName("body")).getText();

                Assert.assertFalse(title.isBlank(),
                        "Expected a page title for " + target);
                Assert.assertFalse(bodyText.isBlank(),
                        "Expected visible content for " + target);

                System.out.println("Visited through ZAP: " + target + " | " + title);
            }
        } finally {
            driver.quit();
        }
    }

    @AfterClass(alwaysRun = true)
    public void collectPassiveResults() throws Exception {
        zap.waitForPassiveScanner();

        for (String target : SecurityConfig.TARGETS) {
            zap.printAlertSummary(target);
        }

        zap.generateHtmlReport("zigwheels-passive-scan.html");

        for (String target : SecurityConfig.TARGETS) {
            zap.enforceQualityGate(target);
        }
    }
}