import org.junit.Before;
import org.junit.Test;
import po.DishesPageObject;
import po.HomePageObject;
import po.LoginPageObject;
import po.MenuPageObject;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * exam
 * NIK on 08/06/2017
 */
public class MyCantinaIT extends WebTestBase {


    @Test
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }

    @Test
    public void createDish(){
        //Arrange
        String userId = getUniqueId();
        createAndLogNewUser(userId,true); // reg & log as a chef
        home.logout();
        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId);

        //Act & Assert

        // 1 from home page, go to the dishes page
        DishesPageObject dishesPage = home.toDishes();
        assertTrue(dishesPage.isOnPage());

        // 2 choose a new unique name
        String dishId = getUniqueDishName();

        // 3 verify no dish exists on the page with that given name
        boolean isDishExistBefore = dishesPage.isDishExists(dishId); // before dish not exists
        assertFalse(isDishExistBefore);

        // 4 create a dish with that name
        dishesPage = dishesPage.createDish(dishId); // after dish exists
        assertTrue(dishesPage.isOnPage());

        // 5 verify that dish is now displayed
        boolean isDishExistAfter = dishesPage.isDishExists(dishId);
        assertTrue(isDishExistAfter);
    }


    @Test
    public void testMenu(){
        //Arrange
            // dishes
        String dishName1 = getUniqueDishName();
        String dishName2 = getUniqueDishName();
        String dishName3 = getUniqueDishName();
            // login
        String userId = getUniqueId();
        createAndLogNewUser(userId,true); // reg & log as a chef
        DishesPageObject dishesPage = home.toDishes();
        assertTrue(dishesPage.isOnPage());

        //Act

        // 1 create 3 new dishes
        dishesPage = dishesPage.createDish(dishName1);
        dishesPage = dishesPage.createDish(dishName2);
        dishesPage = dishesPage.createDish(dishName3);

        // 2 go on the Menu creation page
        HomePageObject home1 = dishesPage.toHome();
        MenuPageObject menuPage = home1.toMenus();
        assertNotNull(menuPage);
        assertTrue(menuPage.isOnPage());

        // 3 verify that those 3 dishes are selectable
        boolean isExists1 = menuPage.isDishExists(dishName1);
        boolean isExists2 = menuPage.isDishExists(dishName2);
        boolean isExists3 = menuPage.isDishExists(dishName3);
        assertTrue(isExists1 && isExists2 && isExists3);

        // 4 select 2 of them
        menuPage.chooseDish(0,true);
        menuPage.chooseDish(1,true);

        // 5 create a menu for today
        Date today= Date.from(Instant.now());
        String dateText = menuPage.parseDate(today);
        home = menuPage.clickCreate(dateText);
        assertTrue(home.isOnPage());

        // 6 on home page, click “show default”
        // to make sure that the created menu for today is displayed
        home.clickDefault();

        // 7 verify that the date of the displayed menu is correct (ie today)



    }

    private void waitMe(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
