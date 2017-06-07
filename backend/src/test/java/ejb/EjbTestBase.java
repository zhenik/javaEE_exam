package ejb;


import entity.Dish;
import entity.Menu;
import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import util.DeleterEJB;

import javax.ejb.EJB;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "entity","ejb", "validation")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected UserEJB userEJB;
    @EJB
    protected RestaurantEJB restaurantEJB;

    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(Menu.class);
        deleterEJB.deleteEntities(Dish.class);
        deleterEJB.deleteEntities(User.class);// doesn't work due to @ElementCollection
    }

    protected boolean createUser(String user,String password){
        return userEJB.createUser(user,password,"a","b","c", false);
    }

    protected boolean createChef(String user, String password){
        return userEJB.createUser(user,password,"a","b","c", true);
    }

    protected Dish createDish(String dishName){
        boolean b1 = restaurantEJB.createDish(dishName, "description");
        Dish dish = restaurantEJB.getDish(dishName);

        assertTrue(b1);
        assertNotNull(dish);
        assertEquals(dishName, dish.getName());

        return dish;
    }


    protected Date getToday(){return Date.from(Instant.now());}

    //http://www.java2s.com/Tutorials/Java_Date_Time/Example/Date/Create_today_tomorrow_and_yesterday_date.htm
    protected Date getTomorrow(){return Date.from(Instant.now().plus(1,ChronoUnit.DAYS));}
    protected Date getYesterday(){return Date.from(Instant.now().minus(1,ChronoUnit.DAYS));}



}
