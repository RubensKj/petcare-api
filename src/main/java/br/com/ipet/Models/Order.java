package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "order_pet_shop")
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private String nameCompany;

    @OneToOne
    private Address companyOrderAddress;

    private String emailOrderUser;

    private BigDecimal total;

    private BigDecimal subTotal;

    @Enumerated(EnumType.STRING)
    private StatusOrder statusOrder;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> servicesIdsCart;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> productsIdsCart;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdOrderAt;

    public Order() {
    }

    public Order(String nameCompany, Address companyOrderAddress, String emailOrderUser, PaymentMethod method, BigDecimal total, BigDecimal subTotal, Set<Long> servicesIdsCart, Set<Long> productsIdsCart) {
        this.nameCompany = nameCompany;
        this.companyOrderAddress = companyOrderAddress;
        this.emailOrderUser = emailOrderUser;
        this.paymentMethod = method;
        this.total = total;
        this.subTotal = subTotal;
        this.servicesIdsCart = servicesIdsCart;
        this.productsIdsCart = productsIdsCart;
    }

    public Long getId() {
        return id;
    }

    public String getNameOfCompany() {
        return nameCompany;
    }

    public String getEmailOrderUser() {
        return emailOrderUser;
    }

    public Address getCompanyOrderAddress() {
        return companyOrderAddress;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public Set<Long> getServicesIdsCart() {
        return servicesIdsCart;
    }

    public Set<Long> getProductsIdsCart() {
        return productsIdsCart;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatusOrder(StatusOrder statusOrder) {
        this.statusOrder = statusOrder;
    }
}
