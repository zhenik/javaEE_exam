package entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Menu {

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateId;

    @Size(min = 1)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    public Menu() {}

    public Date getDateId() {return dateId;}
    public void setDateId(Date dateId) {this.dateId = dateId;}

    public Set<Dish> getDishes() {
//        if (dishes==null){
//            return new HashSet<>();
//        }
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {this.dishes = dishes;}
}
