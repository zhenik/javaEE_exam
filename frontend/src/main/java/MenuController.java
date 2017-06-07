import ejb.RestaurantEJB;
import entity.Menu;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
public class MenuController implements Serializable {

    @EJB
    private RestaurantEJB restaurantEJB;

    public Menu getMenu(){
        Menu menu = restaurantEJB.getCurrentMenu(Date.from(Instant.now()));
        if (menu!=null) System.out.println("TAG menu size: "+menu.getDishes().size());
        return menu;
    }


}
