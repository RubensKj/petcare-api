package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@DynamicUpdate
@Getter @Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 18)
    private String cnpj;

    @NotBlank
    @Size(max = 75)
    private String companyName;
    @Size(max = 350)
    private String description;

    @Size(max = 10)
    private String status;

    @Size(max = 1000)
    private String avatar;

    @Column(precision = 1)
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
    private Map<String, Boolean> userFavorites;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime creationDate;

    public Company() {}

    public Company(@NotNull @Size(max = 14) String cnpj, @NotBlank @Size(min = 3, max = 50) String companyName, @Size(max = 350) String description, @Size(max = 10) String status, String avatar, @DecimalMax("5.0") @DecimalMin("0.0") double rate) {
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.description = description;
        this.status = status;
        this.avatar = avatar;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, Boolean> getUserFavorites() {
        return userFavorites;
    }
}
