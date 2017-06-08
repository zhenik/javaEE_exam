package ejb;

import entity.Dish;
import entity.Menu;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
        dish.setCreatedTime(new Date());
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
    // source:  https://stackoverflow.com/a/12679472/6769651
    private Date getFirstSecondOfDay(Date date){
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }
    private Date localDateTimeToDate(LocalDateTime startOfDay) {
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
    private LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }


    public Menu getCurrentMenu(Date date){
        Date date1 = getFirstSecondOfDay(date);
        Menu menu = em.find(Menu.class,date1);
        if (menu==null){
            menu = getNextMenu(date);
            if (menu==null){
                menu=getPreviousMenu(date);
            }
        }
        return menu;
    }

    public Menu getNextMenu(Date date){
        Menu menu = null;
        Date date1 = getFirstSecondOfDay(date);
        Query query = em.createQuery("select m from Menu m where m.id > ?1 order by m.dateId");
        query.setParameter(1, date1);
        query.setMaxResults(1);
        try {
            menu = (Menu) query.getSingleResult();
        }catch (NoResultException e){
            e.printStackTrace();
        }
        return menu;
    }

    public Menu getPreviousMenu(Date date){
        Menu menu = null;
        Date date1 = getFirstSecondOfDay(date);
        Query query = em.createQuery("select m from Menu m where m.id < ?1 order by m.dateId desc");
        query.setParameter(1, date1);
        query.setMaxResults(1);
        try {
            menu = (Menu) query.getSingleResult();
        }catch (NoResultException e){
            e.printStackTrace();
        }
        return menu;
    }

    public Menu getMenu(Date date){
        Date date1 = getFirstSecondOfDay(date);
        Menu menu = em.find(Menu.class,date1);
        return menu;
    }

}
