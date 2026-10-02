package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * zigwheels.com/used-car/{city} (the brief uses Chennai).
 *
 * Confirmed live via DevTools (23 Sep 2026): the left-hand filter panel has
 * a "Brand and Model" section containing a "Popular Models" list:
 *
 *   <ul class="zw-sr-secLev usedCarMakeModelList popularModels ml-20 mt-10">
 *     <li id="mmvLiBm_8_125">
 *       <label for="bycaridBm8_125">Honda City</label>
 *       ...
 *
 * with 8 models for Chennai (Maruti Suzuki Maruti 800, Maruti Suzuki Maruti
 * Swift, Hyundai I10, Hyundai Santro Xing, Honda City, Toyota Innova, Toyota
 * Fortuner, Mahindra XUV500). This is exactly what the brief asks for
 * ("extract all the popular models in a List; Display the same").
 *
 * Why the earlier version never found it: that list sits in its own small
 * scrolling box, so most items are scrolled out of view. Selenium's getText()
 * only returns text that is visible on screen, so it returned empty strings
 * and the code silently fell back to a different table. Reading each label's
 * textContent instead returns the text whether or not it is scrolled into
 * view. The silent fallback has been removed: if the Popular Models list is
 * missing, the scenario now fails instead of quietly using other data.
 */
public class UsedCarsChennaiPage extends BasePage {

    private static final By POPULAR_MODEL_LABELS = By.cssSelector("ul.popularModels li label");

    private static final By TOP_TEN_TABLE_ROWS =
            By.xpath("//*[contains(text(),'Top 10 Used Cars')]/following::table[1]//tr[td]");

    public UsedCarsChennaiPage(WebDriver driver) {
        super(driver);
    }

    public record PopularModelRow(String modelName, String inventoryCount) {
    }

    /** True when the page loaded is the used cars page for the given city. */
    public boolean isForCity(String city) {
        return driver.getCurrentUrl().toLowerCase().contains("/used-car/" + city.toLowerCase())
                && driver.getTitle().toLowerCase().contains(city.toLowerCase());
    }

    /**
     * Extracts every model in the "Popular Models" filter list into a
     * List<String>.
     */
    public List<String> extractPopularModels() {
        wait.until(d -> !d.findElements(POPULAR_MODEL_LABELS).isEmpty());
        List<String> models = new ArrayList<>();
        for (WebElement label : driver.findElements(POPULAR_MODEL_LABELS)) {
            String text = label.getAttribute("textContent");
            text = text == null ? "" : text.replaceAll("\\s+", " ").trim();
            if (!text.isEmpty() && !models.contains(text)) {
                models.add(text);
            }
        }
        return models;
    }

    /**
     * Extracts the "Top 10 Used Cars In Chennai" table (model name +
     * listing count) as a bonus data set, displayed alongside the Popular
     * Models list. Not used as a substitute for it.
     */
    public List<PopularModelRow> extractTopTenTable() {
        List<WebElement> rows = driver.findElements(TOP_TEN_TABLE_ROWS);
        List<PopularModelRow> table = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                table.add(new PopularModelRow(
                        cells.get(0).getAttribute("textContent").trim(),
                        cells.get(1).getAttribute("textContent").trim()));
            }
        }
        return table;
    }
}
