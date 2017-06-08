import ejb.RestaurantEJB;
import entity.Dish;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named
@RequestScoped
public class DishController implements Serializable {

    @EJB
    private RestaurantEJB restaurantEJB;

    @Inject
    private LoginController loginController;

    private String formDishName;
    private String formDishDescription;

    public DishController() {}

    private void clearFields(){
        formDishName="";
        formDishDescription="";
    }

    public List<Dish> getDishes(){
        if (loginController.isLoggedIn()){
            List<Dish> dishes = restaurantEJB.getDishes();
            Collections.sort(dishes);
            return dishes;
        }
        return null;
    }

    public void createDish(){
//        if (formDishDescription==null || formDishDescription.isEmpty())return;
        boolean b = restaurantEJB.createDish(formDishName, formDishDescription);
        clearFields();
    }

    public String getFormDishName() {return formDishName;}
    public void setFormDishName(String formDishName) {this.formDishName = formDishName;}
    public String getFormDishDescription() {return formDishDescription;}
    public void setFormDishDescription(String formDishDescription) {this.formDishDescription = formDishDescription;}
}
