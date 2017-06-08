package ejb;

import entity.Dish;
import entity.Menu;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class RestaurantEJBTest extends EjbTestBase  {


    /**

      +------------------------------------------------------------+
      |MARKED by task                                              |
      +------------------------------------------------------------+

    */
    @Test
    public void testCreateADish(){
        //Arrange
        String dishName = "dishName";
        String dishDescription = "dishDescription";

        //Act
        boolean created = restaurantEJB.createDish(dishName,dishDescription);

        //Assert
        assertTrue(created);
    }

    @Test
    public void testCreateTwoDishes(){
        //Arrange
        String dishName1 = "foo";
        String dishName2 = "bar";
        String dishDescription = "dishDescription";

        //Act
        boolean created1 = restaurantEJB.createDish(dishName1,dishDescription);
        boolean created2 = restaurantEJB.createDish(dishName2,dishDescription);

        //Assert
        assertTrue(created1);
        assertTrue(created2);
    }

    @Test
    public void testCreateMenuWithNoDish(){
        //Arrange
        Date today1 = getToday();

        //Act
        boolean menuCreated =  restaurantEJB.createMenu(today1);

        //Assert
        assertFalse(menuCreated);
        assertEquals(0, restaurantEJB.getDishes().size());
    }


    @Test
    public void testGetCurrentMenu(){
        //Arrange
        Dish dish = createDish("foo");
        Date today1 = getToday();
        boolean menuCreated =  restaurantEJB.createMenu(today1, dish);

        //Act
        Date today_severalMillsLater = getToday();
        Menu menu = restaurantEJB.getCurrentMenu(today_severalMillsLater);

        //Assert
        assertNotNull(menu);
        assertEquals(1, menu.getDishes().size());

        assertTrue(menuCreated);
        assertEquals(1, restaurantEJB.getDishes().size());
    }

    @Test
    public void testGetAbsentPreviousMenu(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date yesterday = getYesterday();
        Date tomorrow = getTomorrow();

        boolean menuYesterdayCreated =  restaurantEJB.createMenu(yesterday, dish1, dish2);
        assertTrue(menuYesterdayCreated);

        // Act 2 cases
        Menu menu1 = restaurantEJB.getPreviousMenu(tomorrow);
        Menu menu2 = restaurantEJB.getPreviousMenu(yesterday);

        //Assert
        assertNotNull(menu1);
        assertNull(menu2);
        assertEquals(2, menu1.getDishes().size());
    }

    @Test
    public void testGetAbsentNextMenu(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date yesterday = getYesterday();
        Date tomorrow = getTomorrow();

        boolean menuTomorrowCreated =  restaurantEJB.createMenu(tomorrow, dish1, dish2);
        assertTrue(menuTomorrowCreated);

        // Act 2 cases
        Menu menu1 = restaurantEJB.getNextMenu(yesterday);
        Menu menu2 = restaurantEJB.getNextMenu(tomorrow);


        //Assert
        assertNotNull(menu1);
        assertNull(menu2);
        assertEquals(2, menu1.getDishes().size());
    }

    @Test
    public void testGetPreviousMenu(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date yesterday = getYesterday();
        Date tomorrow = getTomorrow();

        boolean menuYesterdayCreated =  restaurantEJB.createMenu(yesterday, dish1, dish2);
        assertTrue(menuYesterdayCreated);

        //Act
        Menu menu1 = restaurantEJB.getPreviousMenu(tomorrow);

        //Assert
        assertNotNull(menu1);
        assertEquals(2, menu1.getDishes().size());
    }

//    create 3 menus, for today, tomorrow and yesterday
//  1 verify that today has tomorrow as next, and yesterday as previous
//  2 verify that tomorrow has no next, and today as previous
//  3 verify that yesterday has no previous, and today as next
    @Test
    public void testThreeMenus(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date yesterday = getYesterday();
        Date tomorrow = getTomorrow();

        //Act
        boolean menuYesterdayCreated =  restaurantEJB.createMenu(yesterday, dish1);
        boolean menuTodayCreated =  restaurantEJB.createMenu(today, dish1, dish2);
        boolean menuTomorrowCreated =  restaurantEJB.createMenu(tomorrow, dish2);
        assertEquals(2,restaurantEJB.getDishes().size());

        // 1 verify that today has tomorrow as next, and yesterday as previous
        Menu menuNext_fromToday = restaurantEJB.getNextMenu(today);
        Menu menuPrevious_fromToday = restaurantEJB.getPreviousMenu(today);
        assertTrue(menuNext_fromToday!=null && menuPrevious_fromToday!=null);
        assertTrue(menuNext_fromToday.getDishes().stream().anyMatch(e->e.getName().equals(dish2.getName())));
        assertTrue(menuPrevious_fromToday.getDishes().stream().anyMatch(e->e.getName().equals(dish1.getName())));

        // 2 verify that tomorrow has no next, and today as previous
        Menu menuNext_fromTomorrow = restaurantEJB.getNextMenu(tomorrow);
        Menu menuPrevious_fromTomorrow = restaurantEJB.getPreviousMenu(tomorrow);
        assertNull(menuNext_fromTomorrow);
        assertNotNull(menuPrevious_fromTomorrow);
        assertTrue(menuPrevious_fromTomorrow.getDishes().stream().anyMatch(e->e.getName().equals(dish1.getName())));
        assertTrue(menuPrevious_fromTomorrow.getDishes().stream().anyMatch(e->e.getName().equals(dish2.getName())));

        // 3 verify that yesterday has no previous, and today as next
        Menu menuPrevious_fromYesterday = restaurantEJB.getPreviousMenu(yesterday);
        Menu menuNext_fromYesterday = restaurantEJB.getNextMenu(yesterday);
        assertNull(menuPrevious_fromYesterday);
        assertNotNull(menuNext_fromYesterday);
        assertTrue(menuNext_fromYesterday.getDishes().stream().anyMatch(e->e.getName().equals(dish1.getName())));
        assertTrue(menuNext_fromYesterday.getDishes().stream().anyMatch(e->e.getName().equals(dish2.getName())));

    }




    /**
     +------------------------------------------------------------+
     | Additional tests                                           |
     +------------------------------------------------------------+


     +------------------------------------------------------------+
     |DISH                                                        |
     +------------------------------------------------------------+

     */
    @Test
    public void testGetDish(){
        //Arrange
        String dishName = "dishName";
        String dishDescription = "dishDescription";
        boolean created = restaurantEJB.createDish(dishName,dishDescription);
        assertTrue(created);

        //Act
        Dish dish1 = restaurantEJB.getDish(dishName);
        Dish dish2 = restaurantEJB.getDish("not exist");

        //Assert
        assertNotNull(dish1);
        assertNull(dish2);
        assertEquals(dishName, dish1.getName());
        assertEquals(dishDescription, dish1.getDescription());
    }

    @Test
    public void testTryCreateDish_dishAlreadyExists(){
        //Arrange
        String dishName = "dishName";
        String dishDescription = "dishDescription";

        //Act
        boolean created1 = restaurantEJB.createDish(dishName,dishDescription);
        boolean created2 = restaurantEJB.createDish(dishName,dishDescription);

        //Assert
        assertTrue(created1);
        assertFalse(created2);
    }

    @Test
    public void testTryCreateDish_wrongId(){
        //Arrange
        String dishName = "      ";
        String dishDescription = "dishDescription";

        // expect exception (entity dish has constraint)
        try {
            restaurantEJB.createDish(dishName,dishDescription);
            fail();
        }catch (EJBException e){}

    }

    @Test
    public void testTryCreateDish_emptyDescription(){
        //Arrange
        String dishName = "name    ";
        String dishDescription = "";

        // expect false
        if (restaurantEJB.createDish(dishName,dishDescription))
            fail();
    }

    @Test
    public void testGetAllDishes(){

        //Arrange
        boolean b1 = restaurantEJB.createDish("fish", "taste fish");
        boolean b2 = restaurantEJB.createDish("meat", "taste meat");
        boolean b3 = restaurantEJB.createDish("meat", "taste meat");

        //Act
        List<Dish> dishes = restaurantEJB.getDishes();

        //Assert
        assertTrue(b1 && b2);
        assertFalse(b3);
        assertEquals(2, dishes.size());
    }


    /**
     +------------------------------------------------------------+
     | MENU                                                       |
     +------------------------------------------------------------+
     */

    @Test
    public void testCreateMenuForToday_oneDish(){
        //Arrange
        Dish dish = createDish("foo");
        Date today = getToday();

        //Act
        boolean menuCreated =  restaurantEJB.createMenu(today, dish);

        //Assert
        assertTrue(menuCreated);
        assertEquals(1, restaurantEJB.getDishes().size());
    }





    @Test
    public void testTryCreateMenu_oneDishNotExist(){
        //Arrange
        Date today1 = getToday();
        Dish dish1 = new Dish();
        dish1.setName("foo");
        dish1.setName("bar");

        Dish dish2 = createDish("foo");
        boolean menuCreated=false;

        //Act
        try {
            menuCreated =  restaurantEJB.createMenu(today1,dish1,dish2);
            fail();
        }catch (EJBException e){}


        //Assert
        assertFalse(menuCreated);
        assertEquals(1, restaurantEJB.getDishes().size());
    }

    @Test
    public void testTryCreate2Menus_sameDate(){
        //Arrange
        Dish dish = createDish("foo");
        Date today1 = getToday();

        //Act
        boolean menuCreated1 =  restaurantEJB.createMenu(today1, dish);
        boolean menuCreated2=false;
        try {
            menuCreated2 =  restaurantEJB.createMenu(today1, dish);
            fail();
        }catch (EJBException e){}


        //Assert
        assertTrue(menuCreated1);
        assertFalse(menuCreated2);

        assertEquals(1, restaurantEJB.getDishes().size());
    }

    @Test
    public void testGetNextMenu(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date tomorrow = getTomorrow();
        Date yesterday = getYesterday();
        boolean menuTodayCreated =  restaurantEJB.createMenu(today, dish1, dish2);
        boolean menuTmrrwCreated =  restaurantEJB.createMenu(tomorrow, dish1, dish2);
        assertTrue(menuTodayCreated);
        assertTrue(menuTmrrwCreated);

        // Act
        Menu menu1 = restaurantEJB.getNextMenu(yesterday);
        Menu menu2 = restaurantEJB.getCurrentMenu(yesterday);

        //Assert
        assertEquals(2, menu1.getDishes().size());
        assertEquals(2, menu2.getDishes().size());
        assertEquals(menu1.getDateId(), menu2.getDateId());
    }

    @Test
    public void testGetCurrentMenu_onlyYesterdayMenuExists(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        Date tomorrow = getTomorrow();
        Date yesterday = getYesterday();
        boolean menuYesterdayCreated =  restaurantEJB.createMenu(yesterday, dish1, dish2);
        assertTrue(menuYesterdayCreated);

        // Act
        Menu menu = restaurantEJB.getCurrentMenu(today);

        //Assert
        assertEquals(2, menu.getDishes().size());
        assertTrue(menu.getDishes().stream().anyMatch(d->d.getName().equals(dish1.getName())));
        assertTrue(menu.getDishes().stream().anyMatch(d->d.getName().equals(dish2.getName())));
    }

    @Test
    public void testGetCurrentMenu_noMenus(){
        //Arrange
        Date today = getToday();

        //Act
        Menu menu = restaurantEJB.getCurrentMenu(today);

        //Assert
        assertNull(menu);
    }

    @Test
    public void testGetMenu(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Date today = getToday();
        boolean menuCreated =  restaurantEJB.createMenu(today, dish1, dish2);
        assertTrue(menuCreated);

        //Act
        Menu menu = restaurantEJB.getMenu(today);

        //Assert
        assertNotNull(menu);
    }


    @Test
    public void testDate(){
        //Arrange
        Dish dish1 = createDish("foo");
        Date today = getToday();
        boolean menuCreated =  restaurantEJB.createMenu(today, dish1);
        assertTrue(menuCreated);
        Menu menu = restaurantEJB.getMenu(today);
        assertNotNull(menu);

        //Act
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate1 = formatter.format(today).trim();
        String formattedDate2 = formatter.format(menu.getDateId()).trim();

        //Assert
        assertTrue(formattedDate1.equals(formattedDate2));
    }

    @Test
    public void testDish_date(){
        //Arrange
        Dish dish1 = createDish("foo");
        Dish dish2 = createDish("bar");
        Dish dish3 = createDish("foofoo");

        //Act
        ArrayList<Dish> list = new ArrayList<>();
        list.add(dish3);
        list.add(dish1);
        list.add(dish2);
        Collections.sort(list);

        //Assert
        assertNotNull(dish1.getCreatedTime());
        assertEquals("foofoo",list.get(0).getName());
        assertEquals("bar",list.get(1).getName());
        assertEquals("foo",list.get(2).getName());
    }

    @Test
    public void testRemoveMenu(){
        //Arrange
        Dish dish = createDish("foo");
        Date today1 = getToday();
        boolean menuCreated =  restaurantEJB.createMenu(today1, dish);
        assertTrue(menuCreated);

        //Act
            // menu exists
        boolean deleted1 = restaurantEJB.removeMenu(getToday());

            // menu not exist
        boolean deleted2 = restaurantEJB.removeMenu(getToday());

        //Assert
        assertTrue(deleted1);
        assertFalse(deleted2);
    }

    @Test
    public void testGetMenus(){
        //Arrange
        Dish dish = createDish("foo");
        Date today = getToday();
        Date tomorrow = getTomorrow();
        boolean menuCreated1 =  restaurantEJB.createMenu(today, dish);
        boolean menuCreated2 =  restaurantEJB.createMenu(tomorrow, dish);
        assertTrue(menuCreated1);
        assertTrue(menuCreated2);

        //Act
        List<Menu> menus = restaurantEJB.getMenus();

        //Assert
        assertEquals(2,menus.size());
    }



}