package br.com.ipet.Models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    public Owner() {
    }

    public Owner(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
