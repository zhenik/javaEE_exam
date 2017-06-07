import ejb.RestaurantEJB;
import entity.Dish;
import entity.Menu;

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
public class MenuCreateController implements Serializable {

    @EJB
    private RestaurantEJB restaurantEJB;

    @Inject
    private LoginController loginController;

    private String date;
    private Map<String, Boolean> menuDishes;

    @PostConstruct
    public void init(){menuDishes=new HashMap<>();}

    public MenuCreateController() {}

    public String getDate() {return null;}
    public void setDate(String date) {this.date = date;}
    public Map<String, Boolean> getMenuDishes() {return menuDishes;}
    public void setMenuDishes(Map<String, Boolean> menuDishes) {this.menuDishes = menuDishes;}

    // Create menu method
    public String save() {

        System.out.println(menuDishes.toString()); //debug

        if (!loginController.isLoggedIn()) return "login.jsf"; // redirect if not logged

        Date date = parseDate();
        if (date == null) {return "menu.jsf";} // date cannot be parsed

        Menu menu = restaurantEJB.getMenu(date);
        if (menu != null) {return "menu.jsf";} // menu already exists

        Dish[] dishesForMenu = getDishesFromMap();
        if (dishesForMenu.length<1)return "menu.jsf";  // no dishes attached

        // Create menu
        boolean created = restaurantEJB.createMenu(date, dishesForMenu);
        System.out.println(created+"");

        menuDishes = new HashMap<>(); // clean map
        return "home.jsf";
    }

    public void updateMap(ValueChangeEvent event){
        String dishName =(String) ((UIInput) event.getSource()).getAttributes().get("dishName");
        Boolean value = (Boolean) event.getNewValue();
        menuDishes.put(dishName,value);
        System.out.println(dishName + "|"+value);
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

    private Dish[] getDishesFromMap() {
        List<Dish> temp = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : menuDishes.entrySet()) {
            if (entry.getValue()) {
                temp.add(restaurantEJB.getDish(entry.getKey()));
            }
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        Dish[] dishes = new Dish[temp.size()];
        temp.toArray(dishes);
        return dishes;
    }
}
