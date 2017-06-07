package ejb;

import entity.Dish;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

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
}
