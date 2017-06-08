package po;

import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * exam
 * NIK on 08/06/2017
 */
public class DishesPageObject extends PageObject {


    public DishesPageObject(WebDriver driver) {
        super(driver);
        assertEquals("Dishes", driver.getTitle());
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Dishes");
    }
}
