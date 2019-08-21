package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
@Getter @Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "cnpj"
        }),
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_addresses",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    @org.springframework.data.annotation.Transient
    private Set<Address> addresses = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> userFavorites;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> products;

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

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Long> getUserFavorites() {
        return userFavorites;
    }

    public Set<Long> getProducts() {
        return products;
    }
}
