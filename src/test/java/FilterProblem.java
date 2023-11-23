import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FilterProblem {
    private static WebDriverWait wait;

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.t-mobile.com/tablets");

        selectFilter("Brands", "Apple", "Samsung", "TCL");
        selectFilter("Deals", "New", "Special offer");
        selectFilter("Operating System", "iPadOS");
//        selectFilter("Brands", "all");
//        selectFilter("all");

//        driver.close();
    }

    private static void selectFilter(String filter, String... subFilter) {
        WebElement dealsElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("mat-expansion-panel-header-2")));
        WebElement brandsElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("mat-expansion-panel-header-3")));
        WebElement osElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("mat-expansion-panel-header-4")));

        switch (filter) {
            case "Deals" -> dealsElement.click();
            case "Brands" -> brandsElement.click();
            case "Operating System" -> osElement.click();
            case "all" -> {
                dealsElement.click();
                brandsElement.click();
                osElement.click();

                List<WebElement> subFilters = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//div[@class='filters']//input[@type = 'checkbox']/parent::span"))
                );
                for (WebElement subFilterEl : subFilters) {
                    subFilterEl.click();
                }

                return;
            }
            default -> throw new IllegalStateException("Unexpected value: " + filter);
        }

        List<WebElement> labels = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//input[@name = '" + filter + "']/parent::span/parent::label//span[@class='filter-display-name']"))
        );

        for (String value : subFilter) {
            if (value.equals("all")) {
                for (WebElement label : labels) {
                    label.click();
                }

                return;
            } else {
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@name = '" + filter + "']/../parent::label//span[text()=' " + value + " ']"))
                ).click();
            }
        }
    }
}
