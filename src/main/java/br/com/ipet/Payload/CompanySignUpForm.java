package br.com.ipet.Payload;

import br.com.ipet.Models.Address;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public Set<String> getRole() {
        return role;
    }
}
