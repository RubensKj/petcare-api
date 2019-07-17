package br.com.ipet.Models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Size(max = 150)
    private String name;
    @Size(max = 300)
    private String description;
    @Size(max = 1000)
    private String avatar;
    private BigDecimal price;
    @ElementCollection
    List<String> ingredients = new ArrayList<>();
    private Calendar shelfLife;
    @CreationTimestamp
    private Date registeredDate;

    public Product() {
    }

    public Product(@NotNull @Size(max = 150) String name, @Size(max = 300) String description, String avatar, BigDecimal price, List<String> ingredients, Calendar shelfLife) {
        this.name = name;
        this.description = description;
        this.avatar = avatar;
        this.price = price;
        this.ingredients = ingredients;
        this.shelfLife = shelfLife;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Calendar getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Calendar shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }
}
