import ejb.RestaurantEJB;
import entity.Menu;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
//        today=Date.from(Instant.now());
//        currentDate=today;
    }

    public Menu getMenu(){
        if (currentMenu!=null) System.out.println("TAG menu size: "+currentMenu.getDishes().size());

        currentMenu = restaurantEJB.getCurrentMenu(Date.from(Instant.now()));
        if (currentMenu!=null){
            System.out.println(currentMenu.getDateId().toString());
            currentDate=currentMenu.getDateId();
        }
//        currentMenu = restaurantEJB.getCurrentMenu(currentDate);
//        if (restaurantEJB.getNextMenu(currentDate)!=null){
//            nextMenu=restaurantEJB.getNextMenu(currentDate);
//        }
//        if (restaurantEJB.getPreviousMenu(currentDate)!=null){
//            previousMenu=restaurantEJB.getPreviousMenu(currentDate);
//        }

        return currentMenu;
    }

    /**
     * Crutch
     * <f:convertDateTime pattern="dd-MM-yyyy" />
     * recognized date -1 day (error)
     * I had to create my own parser for it
     * example:
     * Thu Jun 08 00:00:00 CEST 2017
     *
     * */
    public String getDateAsString(Date date){
//        currentDate = Date.from(Instant.now());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public Date getCurrentDate() {return currentDate;}
    public void setCurrentDate(Date currentDate) {this.currentDate = currentDate;}

}
