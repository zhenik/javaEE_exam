import ejb.RestaurantEJB;
import entity.Menu;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private RestaurantEJB restaurantEJB;

    private Menu currentMenu;
    private Menu nextMenu;
    private Menu previousMenu;
    private Date currentDate;
    private Date today;

    @PostConstruct
    public void init(){
        today=Date.from(Instant.now());
        currentDate=today;
    }

    public MenuController() {}

    public Menu getMenu(){
        debug(); // date control

        currentMenu = restaurantEJB.getCurrentMenu(currentDate);
        if (currentMenu!=null){
            System.out.println(currentMenu.getDateId().toString());
            currentDate=currentMenu.getDateId();
            previousMenu=restaurantEJB.getPreviousMenu(currentMenu.getDateId());
            nextMenu=restaurantEJB.getNextMenu(currentMenu.getDateId());
        }
        return currentMenu;
    }

    private void debug(){
        System.out.println("GET_MENU TODAY|"+today);
        System.out.println("GET_MENU CURRENT|"+currentDate);
    }

    /**
     * Crutch
     * <f:convertDateTime pattern="dd-MM-yyyy" />
     * recognized date -1 day (error)
     * I had to create my own parser for it
     * */
    public String getDateAsString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public Date getCurrentDate() {return currentDate;}
    public void setCurrentDate(Date currentDate) {this.currentDate = currentDate;}
    public Menu getNextMenu() {return nextMenu;}
    public void setNextMenu(Menu nextMenu) {this.nextMenu = nextMenu;}
    public Menu getPreviousMenu() {return previousMenu;}
    public void setPreviousMenu(Menu previousMenu) {this.previousMenu = previousMenu;}



    public void showDefault(){
        currentDate=today;
    }
    public void showNext(){
        if (currentMenu!=null && nextMenu!=null){
            currentDate=nextMenu.getDateId();
        }
    }
    public void showPrevious(){
        if (currentMenu!=null && previousMenu!=null){
            currentDate=previousMenu.getDateId();
        }
    }

    public List<Menu> getMenus(){return restaurantEJB.getMenus();}
    public void removeMenu(Date menuId){restaurantEJB.removeMenu(menuId);}
}
