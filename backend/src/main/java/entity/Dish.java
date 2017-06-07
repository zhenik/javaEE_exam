package entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;


@Entity
public class Dish {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{1,32}")
    private String name;
    private String description;

    public Dish() {}
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description) {this.description = description;}
}