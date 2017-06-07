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
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }
    @Test
    public void testLoginLink(){
        LoginPageObject login = home.toLogin();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject login = home.toLogin();
        HomePageObject home = login.clickLogin(getUniqueId(),"foo");
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLogin(){
        String userId = getUniqueId();
        createAndLogNewUser(userId, "Joe", "Black");
        home.logout();

        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId, "foo");

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        assertTrue(create.isOnPage());

        String userName = getUniqueId();

        HomePageObject home = create.createUser(userName,"foo","foo","Foo","foo");
        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userName));

        home.logout();
        assertFalse(home.isLoggedIn());
    }
}
