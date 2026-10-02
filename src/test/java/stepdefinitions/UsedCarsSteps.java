package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.UsedCarsChennaiPage;

import java.util.List;

public class UsedCarsSteps {

    private static final Logger log = LoggerFactory.getLogger(UsedCarsSteps.class);

    private UsedCarsChennaiPage usedCarsPage;
    private String city;
    private List<String> popularModels;

    @When("I navigate to the used cars page for {string}")
    public void i_navigate_to_the_used_cars_page_for(String city) {
        this.city = city;
        usedCarsPage = NavigationSteps.currentHomePage().goToUsedCars(city);
        Assert.assertTrue(usedCarsPage.isForCity(city),
                "Expected the used cars page for " + city + " but got " + utils.DriverFactory.getDriver().getCurrentUrl());
    }

    @Then("I extract all the popular models into a list")
    public void i_extract_all_the_popular_models_into_a_list() {
        popularModels = usedCarsPage.extractPopularModels();
    }

    @And("the popular models list should not be empty")
    public void the_popular_models_list_should_not_be_empty() {
        Assert.assertFalse(popularModels.isEmpty(),
                "Expected the 'Popular Models' filter list for " + city + " to contain at least one model");
    }

    @And("I display the popular models list")
    public void i_display_the_popular_models_list() {
        StringBuilder text = new StringBuilder("Popular used car models in " + city
                + " (" + popularModels.size() + ", from the 'Popular Models' filter):\n");
        popularModels.forEach(model -> text.append("  - ").append(model).append('\n'));

        var topTen = usedCarsPage.extractTopTenTable();
        if (!topTen.isEmpty()) {
            text.append("Top 10 Used Cars In ").append(city).append(" (bonus data point):\n");
            topTen.forEach(row -> text.append("  - ").append(row.modelName())
                    .append(" (").append(row.inventoryCount()).append(" listings)\n"));
        }

        log.info("\n{}", text);
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log(text.toString());
        }
    }
}
