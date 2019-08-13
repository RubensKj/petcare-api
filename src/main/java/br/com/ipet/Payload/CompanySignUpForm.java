package br.com.ipet.Payload;

import br.com.ipet.Models.Address;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

public class CompanySignUpForm {

    @NotNull
    @Size(max = 18)
    private String cnpj;

    @NotNull
    @OneToOne
    private String ownerEmail;

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

    private int active = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_addresses",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    @org.springframework.data.annotation.Transient
    private Set<Address> addresses = new HashSet<>();

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getRate() {
        return rate;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }
}
