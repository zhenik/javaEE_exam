package po;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * exam
 * NIK on 08/06/2017
 */
public class MenuPageObject extends PageObject {

    public MenuPageObject(WebDriver driver) {
        super(driver);
        assertEquals("Menu", driver.getTitle());
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Menu");
    }

    public boolean isDishExists(String dishName){
        WebElement element = null;
        try {
            element = driver.findElement(
//                    By.xpath("//table[@id='projectTable']//tbody//tr/td[contains(.,'"+dishName+"')]"));
                    By.xpath("//table//tbody//tr/td[contains(.,'"+dishName+"')]"));
        }catch (NoSuchElementException e){}
        return element!=null;
    }

    public void chooseDish(int position, boolean chose){
        WebElement checkbox = driver.findElement(By.id("createMenuForm:dishTable:"+position+":attach"));
        if(chose && checkbox.isSelected()){return;}
        if(!chose && !checkbox.isSelected()){return;}
        checkbox.click();
        waitForPageToLoad();
    }

    public HomePageObject clickCreate(String date){
        setText("createMenuForm:date", date);
        waitMe();
        WebElement createNewMenu = driver.findElement(By.id("createMenuForm:createMenuButton"));
        createNewMenu.click();
        waitForPageToLoad();

        //Menu for giving date already exists
        if (isOnPage()){
            driver.findElement(By.id("homeLink")).click();
            waitForPageToLoad();
        }
        return new HomePageObject(driver);
    }

    // DEBUG purpose
    private void waitMe(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
