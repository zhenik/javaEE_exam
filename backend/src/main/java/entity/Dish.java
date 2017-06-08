package entity;

import validation.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;


@Entity
public class Dish implements Comparable<Dish>{
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{1,32}")
    private String name;
    @NotEmpty
    private String description;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

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

    public Date getCreatedTime() {return createdTime;}
    public void setCreatedTime(Date createdTime) {this.createdTime = createdTime;}

    public int compareTo(Dish o) {
        return -Long.compare(this.createdTime.getTime(), o.createdTime.getTime());
    }
}