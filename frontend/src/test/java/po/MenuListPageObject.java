package po;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


/**
 * exam
 * NIK on 08/06/2017
 */
public class MenuListPageObject extends PageObject {
    public MenuListPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Menu list");
    }

    public void clickDelete(int position){
        WebElement button = driver.findElement(By.id("deleteMenuForm:menuTable:"+position+":deleteMenuButton"));
        button.click();
        waitForPageToLoad();
    }
    public int getNumberOfDisplayedMenus(){
        List<WebElement> elements = new ArrayList<>();
        try{
            elements = driver.findElements(
                    By.xpath("//table[@id='deleteMenuForm:menuTable']//tbody//tr[string-length(text()) > 0]"));
        }catch (NoSuchElementException e){}
        return elements.size();
    }
}
