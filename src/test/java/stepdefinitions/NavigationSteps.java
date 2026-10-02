package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.HomePage;
import utils.ConfigReader;
import utils.DriverFactory;

import java.util.List;

/**
 * Single home for the "Given I am on the zigwheels home page" step, shared
 * by every feature. Cucumber treats an identical step string defined in
 * two classes as an ambiguous match, so this is the one place it lives;
 * the resulting HomePage is exposed via a ThreadLocal so it survives
 * across step-definition classes within the same (parallel) scenario
 * thread without needing a dependency-injection framework.
 *
 * Also home to the "extract menu items into a list" steps (Key Automation
 * Scope item) - lives here rather than in BikesSteps because it operates
 * on the home page, before any scenario navigates away from it.
 */
public class NavigationSteps {

    private static final Logger log = LoggerFactory.getLogger(NavigationSteps.class);

    private static final ThreadLocal<HomePage> HOME_PAGE = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> MENU_ITEMS = new ThreadLocal<>();

    @Given("I am on the zigwheels home page")
    public void i_am_on_the_zigwheels_home_page() {
        DriverFactory.getDriver().get(ConfigReader.get("base.url"));
        HomePage homePage = new HomePage(DriverFactory.getDriver());
        homePage.dismissCookieConsentIfPresent().dismissCityModalIfPresent();
        HOME_PAGE.set(homePage);
    }

    public static HomePage currentHomePage() {
        HomePage page = HOME_PAGE.get();
        if (page == null) {
            throw new IllegalStateException(
                    "No HomePage on this thread yet - the 'I am on the zigwheels home page' step must run first");
        }
        return page;
    }

    /**
     * Key Automation Scope: "Navigating back to home page". Runs at the end
     * of a scenario, after it has moved away from the home page, and uses
     * the shared BasePage.navigateToHomePage() so every page object gets
     * the same way home.
     */
    @And("I navigate back to the home page")
    public void i_navigate_back_to_the_home_page() {
        HomePage homePage = currentHomePage().navigateToHomePage();
        homePage.dismissCityModalIfPresent();
        HOME_PAGE.set(homePage);
    }

    @Then("I should be back on the zigwheels home page")
    public void i_should_be_back_on_the_zigwheels_home_page() {
        String baseUrl = ConfigReader.get("base.url").replaceAll("/+$", "");
        String currentUrl = DriverFactory.getDriver().getCurrentUrl()
                .replaceAll("[?#].*$", "").replaceAll("/+$", "");
        Assert.assertEquals(currentUrl, baseUrl, "Expected to be back on the home page URL");
        Assert.assertTrue(currentHomePage().isLoaded(), "Home page navigation bar did not load after navigating back");
        log.info("Navigated back to the home page: {}", currentUrl);
    }

    @When("I extract the top navigation menu items into a list")
    public void i_extract_the_top_navigation_menu_items_into_a_list() {
        MENU_ITEMS.set(currentHomePage().extractTopNavMenuItems());
    }

    @And("the menu items list should not be empty")
    public void the_menu_items_list_should_not_be_empty() {
        Assert.assertFalse(MENU_ITEMS.get().isEmpty(), "Expected at least one top navigation menu item");
    }

    @And("I display the menu items list")
    public void i_display_the_menu_items_list() {
        log.info("===== zigwheels top navigation menu items =====");
        MENU_ITEMS.get().forEach(item -> log.info(" - {}", item));
    }
}
