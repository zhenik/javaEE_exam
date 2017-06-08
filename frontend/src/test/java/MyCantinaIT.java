import org.junit.Test;
import po.DishesPageObject;
import po.HomePageObject;
import po.LoginPageObject;
import po.MenuPageObject;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private boolean isTodayMenuAlreadyExists(Date date){
        String currentDateMenuExists = home.getCurrentDateId();
        String todaysString = home.parseDateRevert(date);
        if (currentDateMenuExists!=null){
            if (currentDateMenuExists.equals(todaysString)){
                return true;
            }
        }
        return false;
    }

    @Test
    public void testMenu(){

        /**
         * Arrange
         * * */
            // reg && log as a chef
        String userId = getUniqueId();
        createAndLogNewUser(userId,true);
        Date today= Date.from(Instant.now());

        // if menu with today's date exist, test exit
        if (isTodayMenuAlreadyExists(today)){return;}


            // dishes
        String dishName1 = getUniqueDishName();
        String dishName2 = getUniqueDishName();
        String dishName3 = getUniqueDishName();

        DishesPageObject dishesPage = home.toDishes();
        assertTrue(dishesPage.isOnPage());

        /**
         * Act && Assert
         * * */

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
        boolean isExists1 = menuPage.isDishExistsOnMenuForm(dishName1);
        boolean isExists2 = menuPage.isDishExistsOnMenuForm(dishName2);
        boolean isExists3 = menuPage.isDishExistsOnMenuForm(dishName3);
        assertTrue(isExists1 && isExists2 && isExists3);

        // 4 select 2 of them (sorted DESC: than 2 first are: dishName3 & dishName2)
        menuPage.chooseDish(0,true);    // dishName3
        menuPage.chooseDish(1,true);    // dishName2

        // 5 create a menu for today
        String dateText = menuPage.parseDateDirect(today);
        home = menuPage.clickCreate(dateText);
        assertTrue(home.isOnPage());

        // 6 on home page, click “show default”
        // to make sure that the created menu for today is displayed
        home.clickDefault();

        // 7 verify that the date of the displayed menu is correct (ie today)
        String currentDate = home.getCurrentDateId().trim();
        String todaysDate = home.parseDateRevert(today).trim();
        assertEquals(currentDate,todaysDate);

        // 8.1 verify that the 2 selected dishes are displayed in the menu,
        int amountDishes = home.getNumberOfDisplayedDishes();
        assertEquals(2, amountDishes);

        // 8.2 and only those 2.
        assertTrue(home.isDishExists(dishName3));
        assertTrue(home.isDishExists(dishName2));
    }

    @Test
    public void testDifferentDates() {
        //Arrange
        Date today= Date.from(Instant.now());
        Date tomorrow = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        Date yesterday = Date.from(Instant.now().minus(1,ChronoUnit.DAYS));

        // 1 create 3 menus: one for today, one for tomorrow, and one for yesterday
        create3Menus(yesterday,today,tomorrow);

        // 2 on home page click show default to visualize the menu of today, and verify it
        home.clickDefault();
        String currentDate1 = home.getCurrentDateId().trim();
        String todaysDate = home.parseDateRevert(today).trim();
        assertEquals(currentDate1,todaysDate);

        // 3 click next, and verify that the menu of tomorrow is displayed
        home.clickNext();
        String currentDate2 = home.getCurrentDateId().trim();
        String tomorrowsDate = home.parseDateRevert(tomorrow).trim();
        assertEquals(currentDate2,tomorrowsDate);

        // 4 click previous, and verify that the menu of today is displayed
        home.clickPrevious();
        String currentDate3 = home.getCurrentDateId().trim();
        String todaysDate2 = home.parseDateRevert(today).trim(); // I repeat it for readability (line 154)
        assertEquals(currentDate3,todaysDate2);

        // 5 click previous, and verify that the menu of yesterday is displayed
        home.clickPrevious();
        String currentDate4 = home.getCurrentDateId().trim();
        String yesterdayDate = home.parseDateRevert(yesterday).trim();
        assertEquals(currentDate4,yesterdayDate);
    }

    private void waitMe(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void create3Menus(Date yesteday, Date today, Date tomorrow){
        /**
         * Arrange
         * * */
        // reg && log as a chef
        String userId = getUniqueId();
        createAndLogNewUser(userId,true);

        // dishes
        String dishName1 = getUniqueDishName();
        String dishName2 = getUniqueDishName();
        String dishName3 = getUniqueDishName();

        DishesPageObject dishesPage = home.toDishes();
        assertTrue(dishesPage.isOnPage());
        /**
         * Act && Assert
         * * */
        // create 3 new dishes
        dishesPage = dishesPage.createDish(dishName1);
        dishesPage = dishesPage.createDish(dishName2);
        dishesPage = dishesPage.createDish(dishName3);

        // navigate to menu
        home = dishesPage.toHome();
        MenuPageObject menuPage = home.toMenus();
        assertNotNull(menuPage);
        assertTrue(menuPage.isOnPage());

        // yesterday's menu 1 dish
        menuPage.chooseDish(0,true);    // dishName3
        String dateTextYesterday = menuPage.parseDateDirect(yesteday);
        home = menuPage.clickCreate(dateTextYesterday);
        assertTrue(home.isOnPage());

        // today's menu 2 dishes
        menuPage=home.toMenus();
        menuPage.chooseDish(0,true);    // dishName3
        menuPage.chooseDish(1,true);    // dishName2
        String dateTextToday = menuPage.parseDateDirect(today);
        home = menuPage.clickCreate(dateTextToday);
        assertTrue(home.isOnPage());

        // tomorrow's menu 3 dishes
        menuPage=home.toMenus();
        menuPage.chooseDish(0,true);    // dishName3
        menuPage.chooseDish(1,true);    // dishName2
        menuPage.chooseDish(2,true);    // dishName1
        String dateTextTomorrow = menuPage.parseDateDirect(tomorrow);
        home = menuPage.clickCreate(dateTextTomorrow);
        assertTrue(home.isOnPage());
    }


}
