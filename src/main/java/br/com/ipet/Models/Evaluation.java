package br.com.ipet.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Date;

@Entity
@Table(name = "evaluation")
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private Long idOfOrder;

    private Long idFromUserEvaluated;

    private String nameOfUser;

    @Column(precision=1, scale=1)
    @DecimalMax("5.0")
    @DecimalMin("0.0")
    private double rate;

    private String description;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdEvaluationAt;

    public Evaluation() {
    }

    public Evaluation(Long idOfOrder, Long idFromUserEvaluated, String nameOfUser, @DecimalMax("5.0") @DecimalMin("0.0") double rate, String description) {
        this.idOfOrder = idOfOrder;
        this.idFromUserEvaluated = idFromUserEvaluated;
        this.nameOfUser = nameOfUser;
        this.rate = rate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getIdOfOrder() {
        return idOfOrder;
    }

    public Long getIdFromUserEvaluated() {
        return idFromUserEvaluated;
    }

    public double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }
}
