package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DynamicUpdate
@Getter @Setter
@Table(name = "company", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "cnpj"
        }),
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @NotNull
    @Size(max = 18)
    private String cnpj;

    @NaturalId
    @NotNull
    @NotBlank
    @Size(max = 250)
    @Email
    private String email;

    @NotBlank
    @Size(max = 75)
    private String companyName;
    @Size(max = 350)
    private String description;

    @Size(max = 10)
    private String status;

    @Size(max = 1000)
    private String avatar;

    @Column(precision=1, scale=1)
    @DecimalMax("5.0")
    @DecimalMin("0.0")
    private double rate = 5.0;

//    @JoinTable(name = "company_address",
//            joinColumns = @JoinColumn(name = "company_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id"))
    @OneToOne
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> userFavorites;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> orders;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> products;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> services;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> evaluations;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime creationDate;

    public Company() {}

    public Company(@NotNull @Size(max = 18) String cnpj, @NotNull @NotBlank @Size(max = 250) @Email String email, @NotBlank @Size(max = 75) String companyName, @Size(max = 350) String description, @Size(max = 10) String status, @Size(max = 1000) String avatar, @DecimalMax("5.0") @DecimalMin("0.0") double rate) {
        this.cnpj = cnpj;
        this.email = email;
        this.companyName = companyName;
        this.description = description;
        this.status = status;
        this.avatar = avatar;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Long> getUserFavorites() {
        return userFavorites;
    }

    public Set<Long> getOrders() {
        return orders;
    }

    public Set<Long> getProducts() {
        return products;
    }

    public Set<Long> getServices() {
        return services;
    }

    public Set<Long> getEvaluations() {
        return evaluations;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
