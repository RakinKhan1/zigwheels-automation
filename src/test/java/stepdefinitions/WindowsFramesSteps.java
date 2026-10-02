package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.List;

/**
 * "Handling frames" against zigwheels' own iframes. (The "handling windows"
 * half runs for real inside GoogleInvalidLogin.feature, where zigwheels opens
 * Google's sign-in in a separate popup window.)
 */
public class WindowsFramesSteps {

    private static final Logger log = LoggerFactory.getLogger(WindowsFramesSteps.class);

    private List<String> iframes;

    @When("I extract the iframes on the page into a list")
    public void i_extract_the_iframes_on_the_page_into_a_list() {
        iframes = NavigationSteps.currentHomePage().extractIframesIntoList();
    }

    @Then("the iframe list should not be empty")
    public void the_iframe_list_should_not_be_empty() {
        Assert.assertFalse(iframes.isEmpty(), "Expected zigwheels' home page to contain at least one iframe");
        StringBuilder text = new StringBuilder("iframes found on the zigwheels home page: " + iframes.size() + "\n");
        iframes.forEach(f -> text.append("  - ").append(f).append('\n'));
        log.info("\n{}", text);
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log(text.toString());
        }
    }

    @When("I switch into the first iframe on the page")
    public void i_switch_into_the_first_iframe_on_the_page() {
        NavigationSteps.currentHomePage().switchIntoFirstIframe();
    }

    @Then("the driver should be inside the frame")
    public void the_driver_should_be_inside_the_frame() {
        Assert.assertTrue(NavigationSteps.currentHomePage().isInsideAFrame(),
                "Expected the driver's context to be inside an iframe");
        String chosen = NavigationSteps.currentHomePage().chosenFrameDescription();
        log.info("Switched into iframe: {}", chosen);
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log("Switched into iframe: " + chosen);
        }
    }

    @And("I switch back to the main page content")
    public void i_switch_back_to_the_main_page_content() {
        NavigationSteps.currentHomePage().switchBackToMainPage();
    }

    @Then("the driver should be back on the main page")
    public void the_driver_should_be_back_on_the_main_page() {
        Assert.assertFalse(NavigationSteps.currentHomePage().isInsideAFrame(),
                "Expected the driver's context to be back on the main page");
        Assert.assertTrue(NavigationSteps.currentHomePage().isLoaded(),
                "Main page navigation bar not reachable after leaving the frame");
    }
}
