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
    @GeneratedValue
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(unique = true)
    private Date date;

    @Size(min = 1)
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
