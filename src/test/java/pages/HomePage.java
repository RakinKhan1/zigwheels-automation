package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * zigwheels.com home page.
 *
 * Covers three Key Automation Scope items directly against the real site:
 *  - Extract menu items and store in collections: extractTopNavMenuItems().
 *  - Handling windows: the Login box's "Google" button opens Google's
 *    sign-in in a genuine separate popup window (confirmed live on
 *    23 Sep 2026), which continueWithGoogleInPopup() switches to.
 *  - Handling frames: the home page loads real iframes of its own (Google's
 *    consent and advertising frames, 13 of them when checked live on
 *    23 Sep 2026). The frame methods below list them into a collection,
 *    switch into one, prove the driver is inside it, and switch back.
 */
public class HomePage extends BasePage {

    // Confirmed live via DevTools (22 Sep 2026): zigwheels' desktop top nav
    // is <nav class="headerNav"><ul class="h-d-nav">...<li>. The original
    // locator here only looked for <a> tags, which is why it silently
    // returned zero items on a real run - the top-level labels are
    // <span class="c-p ..."> for dropdown items, not <a>. Anchoring on the
    // <li> structure and pulling whichever direct-child element (<a> or
    // <span>) actually holds the label, rather than assuming a tag.
    @FindBy(xpath = "//nav[contains(@class,'headerNav')]//ul[contains(@class,'h-d-nav')]")
    private WebElement navContainer;

    @FindBy(xpath = "//nav[contains(@class,'headerNav')]//ul[contains(@class,'h-d-nav')]/li")
    private List<WebElement> topNavListItems;

    // The "Choose your city" interstitial modal seen on first load
    @FindBy(xpath = "//*[contains(text(),'Choose your city')]/ancestor::*[self::div][1]")
    private WebElement chooseCityModal;

    @FindBy(xpath = "//*[contains(@class,'close') or text()='×' or text()='Skip']")
    private List<WebElement> dismissButtons;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Two DevTools inspections were needed to get this right (22 Sep 2026):
    // the first showed a real <button aria-label="Consent"> in what looked
    // like ordinary markup, but a plain findElement() still couldn't see
    // it. The class names (fc-dialog, fc-button, fc-cta-consent) identify
    // this as Google Funding Choices, whose dialog is mounted inside a
    // Shadow DOM - invisible to Selenium's normal By.xpath/By.cssSelector
    // (a hard platform limitation, not a locator mistake), even though
    // DevTools happily shows it. The fix has to walk shadow roots via
    // JavaScript instead of using a Selenium locator at all.
    private static final String FIND_CONSENT_BUTTON_JS =
            "function findInShadow(root) {" +
            "  var btn = root.querySelector(\"button[aria-label='Consent']\");" +
            "  if (btn) return btn;" +
            "  var all = root.querySelectorAll('*');" +
            "  for (var i = 0; i < all.length; i++) {" +
            "    if (all[i].shadowRoot) {" +
            "      var found = findInShadow(all[i].shadowRoot);" +
            "      if (found) return found;" +
            "    }" +
            "  }" +
            "  return null;" +
            "}" +
            "return findInShadow(document);";

    /**
     * Dismisses zigwheels' GDPR cookie-consent dialog (Google Funding
     * Choices) if it appears - every automated run hits it fresh (no
     * persisted cookies), unlike a manual browser session which only sees
     * it once. See FIND_CONSENT_BUTTON_JS's comment for why this can't use
     * a normal Selenium locator.
     */
    public HomePage dismissCookieConsentIfPresent() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long deadline = System.currentTimeMillis() + 8000;
        while (System.currentTimeMillis() < deadline) {
            Object found = js.executeScript(FIND_CONSENT_BUTTON_JS);
            if (found instanceof WebElement btn) {
                try {
                    // A real (native) Selenium click, NOT a JS-dispatched
                    // one. Confirmed on a real run (22 Sep 2026): the JS
                    // find-logic itself was correct - console-tested and
                    // verified to return this exact button - but clicking
                    // it via executeScript("arguments[0].click()") never
                    // dismissed the dialog. That method produces an
                    // isTrusted:false event; a consent widget is exactly
                    // the kind of thing that may deliberately reject a
                    // synthetic click to stop bots auto-accepting consent
                    // on a user's behalf. A native WebElement.click()
                    // dispatches a real, trusted click through the browser
                    // itself, which a page-level check can't distinguish
                    // from a genuine user click.
                    btn.click();
                } catch (Exception clickIssue) {
                    // Overlay intercepted the native click - fall back to
                    // a JS click rather than give up outright.
                    js.executeScript("arguments[0].click();", btn);
                }
                sleepQuietly(500);
                // Verify it's actually gone rather than trusting one click.
                Object stillThere = js.executeScript(FIND_CONSENT_BUTTON_JS);
                if (!(stillThere instanceof WebElement)) {
                    return this;
                }
            }
            sleepQuietly(300);
        }
        return this;
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    /** Dismisses the city-selection interstitial if it appears; a no-op otherwise. */
    public HomePage dismissCityModalIfPresent() {
        try {
            // A short-lived, separate wait instance - deliberately NOT reusing
            // the shared `wait` field, since FluentWait.withTimeout() mutates
            // and returns the same object: calling it on `wait` here would
            // permanently shrink every later explicit wait on this page to
            // 3 seconds, a subtle bug worth avoiding.
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(chooseCityModal));
            for (WebElement btn : dismissButtons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    break;
                }
            }
        } catch (Exception ignored) {
            // Modal didn't appear this run (e.g. cookie already set) - fine.
        }
        return this;
    }

    /**
     * Extracts every visible top-level navigation label into a List<String>
     * (Key Automation Scope: "Extract menu items ... & store in collections").
     */
    public List<String> extractTopNavMenuItems() {
        waitVisible(navContainer);
        List<String> menuItems = new ArrayList<>();
        for (WebElement li : topNavListItems) {
            String text;
            try {
                // The label is whichever direct child actually holds it -
                // an <a> for a plain link, a <span> for a dropdown toggle -
                // deliberately NOT li.getText(), which would also pick up
                // the nested submenu <ul> if it were ever visible.
                text = li.findElement(By.xpath("./a | ./span")).getText().trim();
            } catch (NoSuchElementException noDirectLabel) {
                text = li.getText().trim();
            }
            if (!text.isEmpty() && !menuItems.contains(text)) {
                menuItems.add(text);
            }
        }
        return menuItems;
    }

    /**
     * True once the home page's own top navigation is rendered. Used to
     * prove "Navigating back to home page" actually landed on the home
     * page, rather than trusting that driver.get() worked.
     */
    public boolean isLoaded() {
        try {
            return waitVisible(navContainer).isDisplayed();
        } catch (Exception notLoaded) {
            return false;
        }
    }

    public UpcomingBikesPage goToUpcomingBikes() {
        driver.get(utils.ConfigReader.get("base.url") + utils.ConfigReader.get("upcoming.bikes.path"));
        return new UpcomingBikesPage(driver);
    }

    public UsedCarsChennaiPage goToUsedCars(String city) {
        driver.get(utils.ConfigReader.get("base.url") + "/used-car/" + city);
        return new UsedCarsChennaiPage(driver);
    }

    // --- Login box and Google popup window (real zigwheels flow) -------

    // Confirmed live via DevTools (23 Sep 2026): the round person icon at
    // the top right is <div id="des_lIcon" class="zw-icon-my-account ..."
    // data-toggle="modal" data-target="#myModal3">, and it opens the
    // "Login/Register to ZigWheels" box.
    @FindBy(css = "div.zw-icon-my-account[data-target='#myModal3']")
    private WebElement accountIcon;

    // Confirmed live (23 Sep 2026): the Google button in that box is
    // <div class="lgn-sc c-p txt-l pl-30 pr-30 googleSignIn"
    // data-track-label="Popup_Login/Register_with_Google">. An older
    // <span id="googleSignIn"> also exists in the page source but sits inside
    // an HTML comment, so it is dead markup and deliberately not used.
    private static final By GOOGLE_BUTTON = By.cssSelector("div.googleSignIn");

    public HomePage openLoginBox() {
        waitClickable(accountIcon).click();
        wait.until(d -> visibleGoogleButton() != null);
        return this;
    }

    public boolean isLoginBoxOpen() {
        return visibleGoogleButton() != null;
    }

    /**
     * Clicks "Google" in the zigwheels Login box, waits for the Google
     * sign-in popup window to open, and switches the driver into it.
     * Returns the zigwheels window handle so the caller can switch back.
     */
    public String continueWithGoogleInPopup() {
        String zigwheelsWindow = driver.getWindowHandle();
        WebElement googleButton = wait.until(d -> visibleGoogleButton());
        googleButton.click();
        switchToNewWindow(zigwheelsWindow);
        waitForUrlContains("accounts.google.com");
        return zigwheelsWindow;
    }

    private WebElement visibleGoogleButton() {
        for (WebElement button : driver.findElements(GOOGLE_BUTTON)) {
            try {
                if (button.isDisplayed()) {
                    return button;
                }
            } catch (Exception staleOrHidden) {
                // try the next match
            }
        }
        return null;
    }

    // --- Real iframes on the zigwheels home page -------------------------

    private static final By IFRAMES = By.tagName("iframe");

    private String chosenFrameDescription;

    /**
     * Extracts every iframe on the page into a List (Key Automation Scope:
     * "Extract ... from frames & store in collections"), described by its
     * id, name and source.
     */
    public List<String> extractIframesIntoList() {
        wait.until(d -> !d.findElements(IFRAMES).isEmpty());
        List<String> frames = new ArrayList<>();
        for (WebElement frame : driver.findElements(IFRAMES)) {
            frames.add(describeFrame(frame));
        }
        return frames;
    }

    /** Switches into the first iframe on the page that accepts a switch. */
    public HomePage switchIntoFirstIframe() {
        for (WebElement frame : driver.findElements(IFRAMES)) {
            String description = describeFrame(frame);
            try {
                switchToFrame(frame);
                chosenFrameDescription = description;
                return this;
            } catch (Exception couldNotSwitch) {
                // Frame was removed or replaced by the ad script mid-switch;
                // make sure we are back on the main page and try the next one.
                switchToDefaultContent();
            }
        }
        throw new NoSuchElementException("No iframe on the page could be switched into");
    }

    public String chosenFrameDescription() {
        return chosenFrameDescription;
    }

    /** True when the driver's current context is a frame, not the top-level page. */
    public boolean isInsideAFrame() {
        return Boolean.TRUE.equals(((JavascriptExecutor) driver)
                .executeScript("return window.self !== window.top;"));
    }

    public HomePage switchBackToMainPage() {
        switchToDefaultContent();
        return this;
    }

    private String describeFrame(WebElement frame) {
        String id = frame.getAttribute("id");
        String name = frame.getAttribute("name");
        String src = frame.getAttribute("src");
        if (src != null && src.length() > 60) {
            src = src.substring(0, 60) + "...";
        }
        return "id=" + (id == null || id.isBlank() ? "(none)" : id)
                + " | name=" + (name == null || name.isBlank() ? "(none)" : name)
                + " | src=" + (src == null || src.isBlank() ? "(none)" : src);
    }
}
