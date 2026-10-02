package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Shared plumbing for every Page Object: waits, and the two "Key
 * Automation Scope" items that don't belong to any one page -
 * "Handling windows & frames" and "Navigating back to home page".
 *
 * Every concrete page extends this and calls PageFactory.initElements
 * in its own constructor, so @FindBy fields stay declarative while the
 * mechanics (waiting, switching context) live in exactly one place.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected List<WebElement> waitVisibleAll(List<WebElement> elements) {
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    // --- Windows & frames handling -----------------------------------

    /**
     * Switches to whichever browser window/tab was not the original handle,
     * e.g. after a click that opens a new tab. Fails fast with a clear
     * message rather than an ambiguous NoSuchWindowException if the tab
     * never actually opened.
     */
    protected void switchToNewWindow(String originalHandle) {
        wait.until(d -> d.getWindowHandles().size() > 1);
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                return;
            }
        }
        throw new NoSuchWindowException("No new window handle appeared alongside " + originalHandle);
    }

    protected void closeCurrentWindowAndSwitchBack(String originalHandle) {
        driver.close();
        driver.switchTo().window(originalHandle);
    }

    protected void switchToFrame(WebElement frameElement) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    protected void switchToFrame(String nameOrId) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // --- Navigation ----------------------------------------------------

    /** "Navigating back to home page" - Key Automation Scope item. */
    public HomePage navigateToHomePage() {
        driver.get(ConfigReader.get("base.url"));
        return new HomePage(driver);
    }
}
