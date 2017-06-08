package po;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class PageObject {

    protected final WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    public abstract boolean isOnPage();

    public void logout(){
        List<WebElement> logout = driver.findElements(By.id("logoutForm:logout"));
        if(! logout.isEmpty()){
            logout.get(0).click();
            waitForPageToLoad();
        }
    }

    public void setText(String id, String text){
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }

    public boolean isLoggedIn(){
        List<WebElement> logout = driver.findElements(By.id("logoutForm:logout"));
        return !logout.isEmpty();
    }

    public boolean isLoggedIn(String user){
        if(!isLoggedIn()){
            return false;
        }

        return driver.findElement(By.id("welcomeMessage")).getText().contains(user);
    }

    protected Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }

    public String parseDateDirect(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public String parseDateRevert(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public boolean isDishExists(String dishName){
        WebElement element = null;
        try {
            element = driver.findElement(
//                    By.xpath("//table[@id='projectTable']//tbody//tr/td[contains(.,'"+dishName+"')]"));
                    By.xpath("//table[@id='dishTable']//tbody//tr/td[contains(.,'"+dishName+"')]"));
        }catch (NoSuchElementException e){}
        return element!=null;
    }

//    public boolean isDishExists(String dishName){
//        WebElement element = null;
//        try {
//            element = driver.findElement(

//                    By.xpath("//table//tbody//tr/td[contains(.,'"+dishName+"')]"));
//        }catch (NoSuchElementException e){}
//        return element!=null;
//    }

    public int getNumberOfDisplayedDishes(){
        List<WebElement> elements = new ArrayList<>();
        try{
            elements = driver.findElements(
                    By.xpath("//table[@id='dishTable']//tbody//tr[string-length(text()) > 0]"));
        }catch (NoSuchElementException e){}
        return elements.size();
    }

}
