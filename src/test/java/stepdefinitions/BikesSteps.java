package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.UpcomingBikesPage;

import java.util.List;

public class BikesSteps {

    private static final Logger log = LoggerFactory.getLogger(BikesSteps.class);

    private UpcomingBikesPage upcomingBikesPage;
    private List<UpcomingBikesPage.Bike> extractedBikes;

    @When("I navigate to the upcoming bikes page")
    public void i_navigate_to_the_upcoming_bikes_page() {
        upcomingBikesPage = NavigationSteps.currentHomePage().goToUpcomingBikes();
    }

    @And("I extract the upcoming bikes filtered by manufacturer {string} and price ceiling {string} Lakh")
    public void i_extract_the_upcoming_bikes_filtered(String manufacturer, String priceCeiling) {
        extractedBikes = upcomingBikesPage.extractUpcomingBikes(manufacturer, Double.parseDouble(priceCeiling));
    }

    @Then("every returned bike should be a Honda model priced under {string} Lakh")
    public void every_returned_bike_should_match_filter(String priceCeiling) {
        double ceiling = Double.parseDouble(priceCeiling);
        Assert.assertFalse(extractedBikes.isEmpty(), "Expected at least one upcoming Honda bike under " + ceiling + " Lakh");
        for (UpcomingBikesPage.Bike bike : extractedBikes) {
            Assert.assertEquals(bike.manufacturer(), "Honda", "Non-Honda bike leaked through the filter: " + bike);
            Assert.assertTrue(bike.priceLakh() < ceiling, "Bike over price ceiling leaked through: " + bike);
        }
    }

    @And("each bike should have a name, a price and an expected launch date")
    public void each_bike_should_have_required_fields() {
        for (UpcomingBikesPage.Bike bike : extractedBikes) {
            Assert.assertFalse(bike.name().isBlank(), "Bike missing a name: " + bike);
            Assert.assertTrue(bike.priceRupees() > 0, "Bike missing a price: " + bike);
            Assert.assertFalse(bike.launchText().isBlank(), "Bike missing a launch date: " + bike);
        }
    }

    @And("I display the extracted bikes list")
    public void i_display_the_extracted_bikes_list() {
        StringBuilder summary = new StringBuilder(
                "Upcoming Honda bikes under 4 Lakh (" + extractedBikes.size() + " found):");
        log.info("===== Upcoming Honda bikes under 4 Lakh =====");
        for (UpcomingBikesPage.Bike bike : extractedBikes) {
            String line = String.format("Name: %s | Price: Rs. %.2f Lakh | Expected Launch: %s",
                    bike.name(), bike.priceLakh(), bike.launchText());
            log.info(line);
            summary.append(" - ").append(line);
        }
        if (Hooks.currentScenario() != null) {
            Hooks.currentScenario().log(summary.toString());
        }
        Assert.assertNotNull(extractedBikes);
    }
}
