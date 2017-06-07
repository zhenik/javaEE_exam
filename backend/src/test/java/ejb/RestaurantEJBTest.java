package ejb;

import entity.Dish;
import entity.Menu;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class RestaurantEJBTest extends EjbTestBase  {


    /**

      +------------------------------------------------------------+
      |DISH                                                        |
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
        Dish dish = createDish();
        Date today = getToday();

        //Act
        boolean menuCreated =  restaurantEJB.createMenu(today, dish);

        //Assert
        assertTrue(menuCreated);
        assertEquals(1, restaurantEJB.getDishes().size());
    }

    @Test
    public void testGetMenuByDate(){
        //Arrange
        Dish dish = createDish();
        Date today1 = getToday();
        boolean menuCreated =  restaurantEJB.createMenu(today1, dish);

        //Act
        Date today_severalMillsLater = getToday();
        Menu menu = restaurantEJB.getMenu(today_severalMillsLater);

        //Assert
        assertNotNull(menu);
        assertEquals(1, menu.getDishes().size());

        assertTrue(menuCreated);
        assertEquals(1, restaurantEJB.getDishes().size());
    }

    @Test
    public void testTryCreateMenu_noDishes(){
        //Arrange
        Date today1 = getToday();

        //Act
        boolean menuCreated =  restaurantEJB.createMenu(today1);

        //Assert
        assertFalse(menuCreated);
        assertEquals(0, restaurantEJB.getDishes().size());
    }

    @Test
    public void testTryCreateMenu_oneDishNotExist(){
        //Arrange
        Date today1 = getToday();
        Dish dish1 = new Dish();
        Dish dish2 = createDish();
        dish1.setName("foo");
        dish1.setName("bar");

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
        Dish dish = createDish();
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

}