package br.com.ipet.Models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Date;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nameUser;

    @Column(precision = 1)
    @DecimalMax("5.0")
    @DecimalMin("0.0")
    private double rate;

    @CreationTimestamp
    private Date dateOfReview;

    public Evaluation() {
    }

    public Evaluation(String nameUser, @DecimalMax("5.0") @DecimalMin("0.0") double rate) {
        this.nameUser = nameUser;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getDateOfReview() {
        return dateOfReview;
    }

    public void setDateOfReview(Date dateOfReview) {
        this.dateOfReview = dateOfReview;
    }
}
