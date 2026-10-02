package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.GoogleLoginPage;
import utils.DriverFactory;

/**
 * Brief scenario 3: "Try to 'Login' with google, give invalid account
 * details & capture the error message", started from zigwheels itself.
 *
 * Also the project's real "Handling windows" example: zigwheels opens
 * Google's sign-in in a separate popup window, the driver switches into it,
 * and afterwards closes it and switches back to the zigwheels window.
 */
public class GoogleLoginSteps {

    private static final Logger log = LoggerFactory.getLogger(GoogleLoginSteps.class);

    private String zigwheelsWindow;
    private GoogleLoginPage googleLoginPage;
    private String capturedErrorMessage;

    @When("I open the zigwheels login box")
    public void i_open_the_zigwheels_login_box() {
        NavigationSteps.currentHomePage().openLoginBox();
        Assert.assertTrue(NavigationSteps.currentHomePage().isLoginBoxOpen(),
                "The zigwheels Login/Register box did not open");
    }

    @And("I choose to continue with Google")
    public void i_choose_to_continue_with_google() {
        zigwheelsWindow = NavigationSteps.currentHomePage().continueWithGoogleInPopup();
        googleLoginPage = new GoogleLoginPage(DriverFactory.getDriver());
    }

    @Then("a Google sign-in popup window should open")
    public void a_google_sign_in_popup_window_should_open() {
        Assert.assertEquals(DriverFactory.getDriver().getWindowHandles().size(), 2,
                "Expected exactly two windows: zigwheels and the Google popup");
        Assert.assertNotEquals(DriverFactory.getDriver().getWindowHandle(), zigwheelsWindow,
                "Driver should be switched into the popup, not still on zigwheels");
        Assert.assertTrue(googleLoginPage.isOnGoogleSignIn(),
                "Popup is not Google's sign-in page: " + DriverFactory.getDriver().getCurrentUrl());
        log.info("Switched into Google sign-in popup window: {}", DriverFactory.getDriver().getCurrentUrl());
    }

    @When("I attempt to log in with an invalid email {string} and password {string}")
    public void i_attempt_to_log_in_with_invalid_details(String email, String password) {
        capturedErrorMessage = googleLoginPage.attemptInvalidLoginAndCaptureError(email, password);
    }

    @Then("Google should show an error message")
    public void google_should_show_an_error_message() {
        Assert.assertNotNull(capturedErrorMessage, "Expected an error/warning message from Google's sign-in flow");
        Assert.assertFalse(capturedErrorMessage.isBlank(), "Captured error message was blank");
    }

    @And("I capture and display the warning message text")
    public void i_capture_and_display_the_warning_message_text() {
        log.info("===== Google invalid login - captured warning message =====");
        log.info("\"{}\"", capturedErrorMessage);
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log("Captured Google error message: \"" + capturedErrorMessage + "\"");
        }
    }

    @When("I close the Google popup and switch back to zigwheels")
    public void i_close_the_google_popup_and_switch_back_to_zigwheels() {
        googleLoginPage.closePopupAndSwitchBackTo(zigwheelsWindow);
    }

    @Then("I should be back on the zigwheels window")
    public void i_should_be_back_on_the_zigwheels_window() {
        Assert.assertEquals(DriverFactory.getDriver().getWindowHandles().size(), 1,
                "The Google popup window should be closed");
        Assert.assertEquals(DriverFactory.getDriver().getWindowHandle(), zigwheelsWindow,
                "Driver should be back on the original zigwheels window");
        Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("zigwheels.com"),
                "Expected to be back on zigwheels, but was on " + DriverFactory.getDriver().getCurrentUrl());
    }
}
