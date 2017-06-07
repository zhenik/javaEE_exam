import ejb.RestaurantEJB;
import entity.Dish;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Named
@SessionScoped
public class MenuController implements Serializable {
    @EJB
    private RestaurantEJB restaurantEJB;

    @Inject
    private LoginController loginController;

    private String date;
    private Map<String, Boolean> menuDishes;


    @PostConstruct
    public void init(){
        menuDishes=new HashMap<>();
    }

    public MenuController() {}

    public String getDate() {return null;}
    public void setDate(String date) {
        this.date = date;
    }

    private Date parseDate(){
        Date date1 = null;
        if (date!=null){
            System.out.println(date);
            String dateStr = date;
            DateFormat formatter = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            try {
                date1 = (Date)formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date1.getTime());
        }
        return date1;
    }

    public String save() {
        System.out.println(menuDishes.toString());
        menuDishes=new HashMap<>();
        return "menu.jsf";
    }

    public Map<String, Boolean> getMenuDishes() {return menuDishes;}
    public void setMenuDishes(Map<String, Boolean> menuDishes) {this.menuDishes = menuDishes;}


    public void updateMap(ValueChangeEvent event){
        String dishName =(String) ((UIInput) event.getSource()).getAttributes().get("dishName");
        Boolean value = (Boolean) event.getNewValue();
        menuDishes.put(dishName,value);
        System.out.println(dishName + "|"+value);

    }
}
