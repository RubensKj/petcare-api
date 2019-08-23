package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user_pet_care",uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        }),
        @UniqueConstraint(columnNames = {
                "cpf"
        })
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotNull
    @NotBlank
    @Size(max = 250)
    @Email
    @Column(columnDefinition = "varchar(250) default 'Não informado!'")
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_pet_care_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Size(min = 3, max = 255)
    private String completeName;

    @Size(max = 14)
    @Column(columnDefinition = "varchar(15) default 'Não informado!'")
    private String cpf;

    @Size(max = 15)
    @Column(columnDefinition = "varchar(20) default 'Não informado!'")
    private String phoneNumber;

//    @JoinTable(name = "user_addresses",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id"))

    @ManyToMany(fetch = FetchType.EAGER)
    @Transient
    private List<Address> address = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> favorites = new HashSet<>();

    @Size(max = 1000)
    @Column(columnDefinition = "varchar(1000) default 'https://decoradornet.com.br/upload/images/default-avatar.jpg'")
    private String avatar;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime joinedDate;

    public User() {
    }

    public User(String email, String password, String completeName, String cpf, String phoneNumber, String avatar) {
        this.email = email;
        this.password = password;
        this.completeName = completeName;
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public Set<Long> getFavorites() {
        return favorites;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }
}
