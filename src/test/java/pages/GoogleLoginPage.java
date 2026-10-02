package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

/**
 * Google's sign-in page, reached the way the brief describes: from
 * zigwheels' own "Login/Register" box, whose "Google" button opens this page
 * in a separate popup window (confirmed live, 23 Sep 2026). HomePage
 * switches the driver into that popup; this page object then works inside
 * it, and closePopupAndSwitchBackTo() returns to the zigwheels window.
 * Used for the brief's negative-test scenario ("Try to 'Login' with google,
 * give invalid account details & capture the error message").
 *
 * Locators are text/attribute-anchored against Google's well-documented,
 * long-stable sign-in DOM rather than guessed CSS classes, since this
 * session had no live browser access to click through the flow today.
 * Google's identifier field (#identifierId) and the "Next" button's
 * structure have been stable across years of the standard sign-in flow;
 * the README's verification checklist covers reconfirming this once,
 * live, before the demo.
 *
 * Handles both branches an invalid login can take:
 *   1. Email doesn't correspond to any account -> Google shows
 *      "Couldn't find your Google Account" on the SAME step (never
 *      reaches a password field).
 *   2. Email is a real-looking account but the password is wrong ->
 *      Google shows "Wrong password. Try again." on the password step.
 * Both are legitimate "invalid login" outcomes, so this page captures
 * whichever one the flow actually produces rather than assuming one path.
 */
public class GoogleLoginPage extends BasePage {

    @FindBy(id = "identifierId")
    private WebElement emailInput;

    @FindBy(id = "identifierNext")
    private WebElement nextButton;

    @FindBy(name = "Passwd")
    private WebElement passwordInput;

    @FindBy(id = "passwordNext")
    private WebElement passwordNextButton;

    // Text-anchored rather than a jsname/class attribute, which Google
    // rotates between releases; the rendered wording is the stable contract.
    // Confirmed live, 21 Sep 2026 (manual check AND a saved failure
    // screenshot): the real wording is "Couldn't find this account" and it
    // genuinely renders on screen - the earlier version of this locator
    // still failed to match it though, root-caused to Google's apostrophe
    // being a Unicode curly quote (') rather than the plain ASCII one (')
    // used in that string literal. Two visually-identical characters, two
    // different code points, zero XPath match - a real gotcha worth
    // remembering. Matching on "find this account" instead (no apostrophe
    // anywhere in the match) sidesteps the whole character-encoding trap.
    private static final By IDENTIFIER_ERROR =
            By.xpath("//*[contains(text(),'find this account') or " +
                     "contains(text(),'find your Google Account') or " +
                     "contains(text(),'Enter a valid email') or " +
                     "contains(text(),'sign you in') or " +
                     "contains(text(),'may not be secure')]");

    private static final By PASSWORD_ERROR =
            By.xpath("//*[contains(text(),'Wrong password') or contains(text(),\"That password didn't work\")]");

    public GoogleLoginPage(WebDriver driver) {
        super(driver);
    }

    public GoogleLoginPage enterEmail(String email) {
        waitVisible(emailInput).clear();
        emailInput.sendKeys(email);
        return this;
    }

    public GoogleLoginPage clickNext() {
        waitClickable(nextButton).click();
        return this;
    }

    /**
     * Attempts the invalid-credentials flow end to end and returns whatever
     * error message Google actually surfaces (identifier-step or
     * password-step), rather than assuming which branch will fire.
     */
    public String attemptInvalidLoginAndCaptureError(String email, String password) {
        enterEmail(email).clickNext();

        // Give Google's client-side validation a moment to decide which
        // branch to take before we look for either outcome. Uses its own
        // short-lived WebDriverWait rather than mutating the shared `wait`
        // field's timeout (FluentWait.withTimeout mutates and returns the
        // same instance, which would otherwise leak a shorter timeout into
        // every later wait on this page object).
        //
        // Confirmed on a real run (21 Sep 2026): the correct error DOES
        // appear ("Couldn't find this account", verified via a saved
        // failure screenshot) but not within 8 seconds - Google visibly
        // added extra delay before showing it, consistent with treating
        // an automated ChromeDriver session as suspicious and throttling
        // it. 25 seconds gives that delay real room without the scenario
        // hanging indefinitely if something is actually wrong.
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(25)).until(driver ->
                    !driver.findElements(IDENTIFIER_ERROR).isEmpty()
                            || isPasswordStepShown());
        } catch (Exception timeout) {
            // fall through - one of the two checks below will surface the real state
        }

        if (!driver.findElements(IDENTIFIER_ERROR).isEmpty()) {
            return driver.findElement(IDENTIFIER_ERROR).getText().trim();
        }

        if (isPasswordStepShown()) {
            waitVisible(passwordInput).sendKeys(password);
            waitClickable(passwordNextButton).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_ERROR));
            return driver.findElement(PASSWORD_ERROR).getText().trim();
        }

        throw new NoSuchElementException(
                "Neither the identifier-step nor password-step error message appeared - " +
                "Google's sign-in DOM may have changed since this page object was written.");
    }

    /** True when the driver is currently on a Google sign-in page. */
    public boolean isOnGoogleSignIn() {
        return driver.getCurrentUrl().contains("accounts.google.com");
    }

    /**
     * Closes the Google popup window and switches the driver back to the
     * zigwheels window it was opened from.
     */
    public void closePopupAndSwitchBackTo(String zigwheelsWindow) {
        closeCurrentWindowAndSwitchBack(zigwheelsWindow);
    }

    private boolean isPasswordStepShown() {
        try {
            return passwordInput.isDisplayed();
        } catch (Exception notPresentYet) {
            return false;
        }
    }
}
