import com.microsoft.playwright.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightFilterProblem {

    static Page page;

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false).setSlowMo(1000));
            BrowserContext context = browser.newContext();
            page = context.newPage();

            page.navigate("https://www.t-mobile.com/tablets");
//            selectFilter("Brands", "Apple", "Samsung", "TCL");
//            selectFilter("Deals", "New", "Special offer");
//            selectFilter("Operating System", "iPadOS");
//            selectFilter("Brands", "all");
//            selectFilter("all");

//        page.close();
        }
    }

    private static void selectFilter(String filter, String...subFilter) {

        switch (filter) {
            case "Deals" -> page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Deals")).click();
            case "Brands" -> page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Brands")).click();
            case "Operating System" -> page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Operating System")).click();
            case "all" -> {
                page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Deals")).click();
                page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Brands")).click();
                page.getByTestId("desktop-filter-group-name").filter(new Locator.FilterOptions().setHasText("Operating System")).click();

                List<Locator> locators = page.locator(".filter-display-name").all();
                for (Locator locator : locators) {
                    locator.check();
                }
                return;

            }
            default -> throw new IllegalStateException("Unexpected value: " + filter);
        }

        for (String subFltr : subFilter) {
            if (subFltr.equals("all")) {
                List<Locator> subFilters = page.locator(".filter-display-name").all();
                for (Locator locator : subFilters) {
                    locator.check();
                }
            } else {
                page.locator(".filter-display-name").getByText(subFltr).check();
            }
        }
    }
}
