package br.com.ipet.Models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(max = 14)
    private String cnpj;

    @NotBlank
    @Size(min = 3, max = 50)
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
    private List<Address> addresses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_products",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @org.springframework.data.annotation.Transient
    private Set<Product> productsList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_users",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @org.springframework.data.annotation.Transient
    private Set<User> userList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_users",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "evaluation_id"))
    @org.springframework.data.annotation.Transient
    private Set<Evaluation> evaluations = new HashSet<>();

    public Company() {
    }

    public Company(@NotNull @Size(max = 14) String cnpj, @NotBlank @Size(min = 3, max = 50) String companyName, @Size(max = 350) String description, @Size(max = 10) String status, String avatar, @DecimalMax("5.0") @DecimalMin("0.0") double rate, List<Address> addresses, Set<Product> productsList, Set<User> userList, Set<Evaluation> evaluations) {
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.description = description;
        this.status = status;
        this.avatar = avatar;
        this.rate = rate;
        this.addresses = addresses;
        this.productsList = productsList;
        this.userList = userList;
        this.evaluations = evaluations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(Set<Product> productsList) {
        this.productsList = productsList;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
