import org.junit.Before;
import org.junit.Test;
import po.CreateUserPageObject;
import po.HomePageObject;
import po.LoginPageObject;


import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;


public class WebPageIT extends WebTestBase {
    @Before
    public void startFromInitialPage() {

        assumeTrue(JBossUtil.isJBossUpAndRunning());

        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }


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
    public void testLoginCustomer(){
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
//
//    @Test
//    public void testCreateValidUser(){
//        LoginPageObject login = home.toLogin();
//        CreateUserPageObject create = login.clickCreateNewUser();
//        assertTrue(create.isOnPage());
//
//        String userName = getUniqueId();
//
//        HomePageObject home = create.createUser(userName,"foo","foo","Foo","foo");
//        assertNotNull(home);
//        assertTrue(home.isOnPage());
//        assertTrue(home.isLoggedIn(userName));
//
//        home.logout();
//        assertFalse(home.isLoggedIn());
//    }
}
