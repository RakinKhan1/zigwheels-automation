package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * zigwheels.com/upcoming-bikes.
 *
 * Locators here are NOT guesses: they are the exact structure confirmed
 * live via browser DevTools during this project's JMeter performance-test
 * reconnaissance (10 Sep 2026), the same reconnaissance that showed the
 * hackathon brief's own suggested markup (`Bike Name:\s*<strong>`) does not
 * match the real page and would silently extract nothing. That finding
 * carries over directly to this Selenium layer: we anchor on the same
 * verified attributes rather than the brief's guessed pattern.
 *
 * Each bike card is an <li> that carries clean machine-readable attributes
 * BEFORE the display text is committed to the DOM:
 *   data-price="280000"          (raw rupee integer)
 *   data-explaunch="1788201000"  (unix timestamp)
 * Name sits on the model-link <a title="Honda CB 500" data-track-label="model-name">.
 */
public class UpcomingBikesPage extends BasePage {

    private static final By MANUFACTURER_LINK = By.cssSelector("a[title][data-track-label='model-name']");
    private static final By LAUNCH_TEXT = By.xpath(".//div[contains(text(),'Expected Launch')]");

    @FindBy(css = "li[data-price]")
    private List<WebElement> bikeCards;

    public UpcomingBikesPage(WebDriver driver) {
        super(driver);
    }

    public record Bike(String name, double priceLakh, long priceRupees, String launchText, String manufacturer) {
    }

    /**
     * Extracts every upcoming bike card, filters to the requested
     * manufacturer and price ceiling, and returns them as a List
     * (Problem Statement: Honda, under 4 Lakh, name + price + launch date).
     */
    public List<Bike> extractUpcomingBikes(String manufacturerFilter, double priceCeilingLakh) {
        // Waiting for EVERY one of ~97 cards to be visible at once (the
        // original approach) hangs indefinitely if the page lazy-renders
        // off-screen rows in a long list, which is what happened on a real
        // run against this page. We only need the first card to exist
        // before iterating; a per-card try/catch below already tolerates
        // any individual card that isn't fully rendered yet.
        wait.until(d -> !bikeCards.isEmpty());
        List<Bike> result = new ArrayList<>();

        for (WebElement card : bikeCards) {
            try {
                WebElement nameLink = card.findElement(MANUFACTURER_LINK);
                String fullName = nameLink.getAttribute("title"); // e.g. "Honda CB 500"

                String rawPrice = card.getAttribute("data-price"); // e.g. "280000"
                if (rawPrice == null || rawPrice.isBlank()) continue;
                long priceRupees = Long.parseLong(rawPrice.trim());
                double priceLakh = priceRupees / 100000.0;

                String launchText = card.findElement(LAUNCH_TEXT).getText()
                        .replaceAll("(?i)Expected Launch\\s*:\\s*", "").trim();

                String manufacturer = fullName.split("\\s+")[0];

                if (!manufacturer.equalsIgnoreCase(manufacturerFilter)) continue;
                if (priceLakh >= priceCeilingLakh) continue;
                // A real, confirmed data gap on the live site (found on an
                // actual run, e.g. "Honda Shine Electric"): some models have
                // the "Expected Launch" div present but empty, with no
                // confirmed month yet. The brief asks to display name, price
                // AND launch date, so a bike missing that field isn't a
                // complete record to display - skip it here rather than
                // return a bike with a blank date.
                if (launchText.isBlank()) continue;

                result.add(new Bike(fullName, priceLakh, priceRupees, launchText, manufacturer));
            } catch (Exception cardIssue) {
                // A single malformed card (missing field) shouldn't sink the whole
                // extraction - skip it and keep going, same defensive pattern used
                // in the JMeter Regular Expression Extractors for this same page.
            }
        }
        return result;
    }
}
