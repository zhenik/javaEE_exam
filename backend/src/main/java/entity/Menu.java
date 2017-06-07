package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(unique = true)
    private Date date;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    public Menu() {}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    public Set<Dish> getDishes() {
        if (dishes==null){
            return new HashSet<>();
        }
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {this.dishes = dishes;}
}
