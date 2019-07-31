package br.com.ipet.Models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int placeNumber;
    @Size(max = 100)
    private String complement;
    @Column(length = 60)
    private String street;
    private String neighborhood;

    @Size(max = 10)
    private String cep;

    @Size(max = 100)
    private String city;
    private String state;

    private String latitude;
    private String longitude;

}
