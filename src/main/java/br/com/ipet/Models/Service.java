package br.com.ipet.Models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "service")
@DynamicUpdate
@Getter
@Setter
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @NotNull
    @Size(max = 65)
    private String name;

    @Size(max = 650)
    private String description;

    @NotNull
    private BigDecimal price;

    public Service() {
    }

    public Service(@NotNull @Size(max = 65) String name, @Size(max = 650) String description, @NotNull BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }
}
