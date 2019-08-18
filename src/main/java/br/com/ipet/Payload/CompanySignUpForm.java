package br.com.ipet.Payload;

import br.com.ipet.Models.Address;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

public class CompanySignUpForm {

    @NotNull
    private String email;

    @NotNull
    private String completeName;

    @NotNull
    private String password;

    @NotNull
    private String cpf;

    @NotNull
    private String phoneNumber;

    @NotNull
    @Size(max = 18)
    private String cnpj;

    @NotNull
    @Size(max = 75)
    private String companyName;
    @Size(max = 350)
    private String description;

    private int active = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_addresses",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    @org.springframework.data.annotation.Transient
    private Set<Address> addresses = new HashSet<>();

    private Set<String> role;

    public String getEmail() {
        return email;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getPassword() {
        return password;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public int getActive() {
        return active;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Set<String> getRole() {
        return role;
    }
}
