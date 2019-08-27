package br.com.ipet.Payload;

import br.com.ipet.Models.Address;

import javax.validation.constraints.NotNull;

public class CompanyEditForm {

    @NotNull
    private String cnpj;

    @NotNull
    private String companyName;

    @NotNull
    private String description;

    @NotNull
    private Address address;

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }
}
