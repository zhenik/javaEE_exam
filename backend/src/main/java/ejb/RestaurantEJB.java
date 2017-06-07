package ejb;

import entity.Dish;
import entity.Menu;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

@Stateless
public class RestaurantEJB implements Serializable {
    @PersistenceContext
    private EntityManager em;

    public boolean createDish(String name, String description){
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            return false;
        }

        Dish dish = getDish(name);
        if (dish != null) {
            //a dish with same name(id) already exists
            return false;
        }

        dish = new Dish();
        dish.setName(name);
        dish.setDescription(description);
        em.persist(dish);
        return true;
    }

    public Dish getDish(String name) {return em.find(Dish.class, name);}

    public List<Dish> getDishes(){
        Query query = em.createQuery("SELECT d FROM Dish d");
        List<Dish> dishes = query.getResultList();
        return dishes;
    }

    public boolean createMenu(Date date, Dish... dishes){
        if (date==null || dishes.length<1){
            return false;
        }

        Set<Dish> menuDishes = new HashSet<>();
        for (Dish dish:dishes){
            Dish dishFromDb = getDish(dish.getName());
            if (dishFromDb!=null){
                menuDishes.add(dishFromDb);
            }
            else{
                throw new IllegalArgumentException("Dish with name: "+dish.getName()+ " does not exists in database");
            }
        }
        Date date1 = getFirstSecondOfDay(date);
        Menu menu = new Menu();
        menu.setDateId(date1);
        menu.setDishes(menuDishes);
        em.persist(menu);
        return true;
    }

    // I fetch first millisecond (beginning of the day)
    // the given date and persist it as dateId
    private Date getFirstSecondOfDay(Date date){
        Calendar c = GregorianCalendar.getInstance();
        c.clear();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR);
        // need to clear day milliseconds
        c.clear();
        c.set(Calendar.DAY_OF_YEAR, day);
        c.set(Calendar.YEAR, year);

        return c.getTime();
    }

    public Menu getMenu(Date date){
        Date date1 = getFirstSecondOfDay(date);
        return em.find(Menu.class,date1);
    }

}
