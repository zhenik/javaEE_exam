package po;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePageObject extends PageObject {

    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/my_cantina"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }


    public boolean isOnPage() {
        return driver.getTitle().equals("MyCantina Home Page");
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }

    public DishesPageObject toDishes(){
        driver.findElement(By.id("dishesLink")).click();
        waitForPageToLoad();
        return new DishesPageObject(driver);
    }

    public boolean isLinkDishesVisible(){
        WebElement linkToDishes=null;
        try{
            linkToDishes = driver.findElement(By.id("dishesLink"));
        }catch (NoSuchElementException e){}
        return linkToDishes!=null;
    }

    public boolean isLinkMenusVisible(){
        WebElement linkToMenus=null;
        try{
            linkToMenus = driver.findElement(By.id("menusLink"));
        }catch (NoSuchElementException e){}

        return linkToMenus!=null;
    }

    public boolean isChef(){
        WebElement welcomeMessage=null;
        try{
            welcomeMessage = driver.findElement(By.xpath("//span[@id='welcomeMessage']"));
            if (welcomeMessage.getText().contains("Chef")){return true;}
        }catch (NoSuchElementException e){}
        return false;
    }

    public boolean isCustomer(){
        WebElement welcomeMessage=null;
        try{
            welcomeMessage = driver.findElement(By.xpath("//span[@id='welcomeMessage']"));
            if (welcomeMessage.getText().contains("Customer")){return true;}
        }catch (NoSuchElementException e){}
        return false;
    }


//    public int getNumberOfDisplayedEvents() {
//        List<WebElement> elements = driver.findElements(
//                By.xpath("//table[@id='eventTable']//tbody//tr[string-length(text()) > 0]"));
//
//        return elements.size();
//    }
}
