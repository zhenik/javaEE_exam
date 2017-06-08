import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import po.*;


import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;


public class WebPageIT extends WebTestBase {

    @Test
    public void testLoginLink(){
        //Act
        LoginPageObject login = home.toLogin();

        //Assert
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        //Arrange
        LoginPageObject login = home.toLogin();

        //Act
        HomePageObject home = login.clickLogin(getUniqueId());

        //Assert
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testRegister_wrongPasswordConfirmation(){
        //Arrange
        String userId = getUniqueId();

        //Act
        createAndLogNewUserManually(userId,
                "passwords",
                "no",
                "firstName",
                "middlename",
                "lastName",
                false);
        //Assert
        assertFalse(home.isLoggedIn());

    }

    @Test
    public void testRegister_withSameUserId(){
        //Arrange
        String userId = getUniqueId();

        //Act
        createAndLogNewUser(userId,false);
        home.logout();

        createAndLogNewUserManually(userId,
                "passwords",
                "passwords",
                "firstName",
                "middlename",
                "lastName",
                false);

        //Assert
        assertFalse(home.isLoggedIn());




    }


    @Test
    public void testRegisterAndLoginCustomer(){
        //Arrange
        String userId = getUniqueId();

        //Act
        createAndLogNewUser(userId,false);
        home.logout();
        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId);

        boolean isLinkToDishes = home.isLinkDishesVisible();
        boolean isLinkMenus = home.isLinkMenusVisible();
        boolean isChef = home.isChef();
        boolean isCustomer = home.isCustomer();

        //Assert
        assertFalse(isLinkToDishes);
        assertFalse(isLinkMenus);
        assertFalse(isChef);
        assertTrue(isCustomer);

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testRegisterAndLoginLoginChef(){
        //Arrange
        String userId = getUniqueId();

        //Act
        createAndLogNewUser(userId,true);
        home.logout();
        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId);

        boolean isLinkToDishes = home.isLinkDishesVisible();
        boolean isLinkMenus = home.isLinkMenusVisible();
        boolean isChef = home.isChef();
        boolean isCustomer = home.isCustomer();

        //Assert
        assertTrue(isLinkToDishes);
        assertTrue(isLinkMenus);
        assertTrue(isChef);
        assertFalse(isCustomer);

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testDishesLink_3Cases(){

        // 1 Act & Assert NON registered
        DishesPageObject dishesPage=null;
        try{
            dishesPage = home.toDishes();
            fail();
        }catch (NoSuchElementException e){}
        assertNull(dishesPage);

        // 2 Act & Assert CUSTOMER
        try{
            String userId = getUniqueId();
            createAndLogNewUser(userId,false);

            dishesPage = home.toDishes();
            fail();
        }catch (NoSuchElementException e){}
        assertNull(dishesPage);

        // 3 Act & Assert CHEF
        String userId = getUniqueId();
        createAndLogNewUser(userId,true);
        dishesPage = home.toDishes();

        assertNotNull(dishesPage);
        assertTrue(dishesPage.isOnPage());
    }

    @Test
    public void testMenuLink_3Cases(){

        // 1 Act & Assert NON registered
        MenuPageObject menuPage=null;
        try{
            menuPage = home.toMenus();
            fail();
        }catch (NoSuchElementException e){}
        assertNull(menuPage);

        // 2 Act & Assert CUSTOMER
        try{
            String userId = getUniqueId();
            createAndLogNewUser(userId,false);
            menuPage = home.toMenus();
            fail();
        }catch (NoSuchElementException e){}
        assertNull(menuPage);

        // 3 Act & Assert CHEF
        String userId = getUniqueId();
        createAndLogNewUser(userId,true);
        menuPage = home.toMenus();

        assertNotNull(menuPage);
        assertTrue(menuPage.isOnPage());
    }

}
