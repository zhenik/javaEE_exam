package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePageObject extends PageObject {

    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/exam"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }


    public boolean isOnPage() {
        return driver.getTitle().equals("Exam Home Page");
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }




    public int getNumberOfDisplayedEvents() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='eventTable']//tbody//tr[string-length(text()) > 0]"));

        return elements.size();
    }
}
