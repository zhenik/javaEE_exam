package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * exam
 * NIK on 06/06/2017
 */
public class CreateUserPageObject extends PageObject {
    public CreateUserPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create User", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create User");
    }

    public HomePageObject createUser(String userId, String password, String confirmPassword,
                                     String firstName,String middlename, String lastName, Boolean chef){

        setText("createUserForm:userName",userId);
        setText("createUserForm:password",password);
        setText("createUserForm:confirmPassword",confirmPassword);
        setText("createUserForm:firstName",firstName);
        setText("createUserForm:middleName",middlename);
        setText("createUserForm:lastName",lastName);
        if (chef){driver.findElement(By.id("createUserForm:chef")).click();}
//        try {
//            new Select(driver.findElement(By.id("createUserForm:country"))).selectByVisibleText(country);
//        } catch (Exception e){
//            return null;
//        }

        driver.findElement(By.id("createUserForm:createButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }
}
