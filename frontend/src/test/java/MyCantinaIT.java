import org.junit.Before;
import org.junit.Test;
import po.DishesPageObject;
import po.HomePageObject;
import po.LoginPageObject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
}
