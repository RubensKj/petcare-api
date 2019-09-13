package br.com.ipet.Payload;

public class EvaluationWithCNPJForm {

    private String cnpj;
    private Long idOfOrder;
    private Long idFromUserEvaluated;
    private String nameOfUser;
    private double rate;
    private String description;

    public String getCnpj() {
        return cnpj;
    }

    public Long getIdOfOrder() {
        return idOfOrder;
    }

    public Long getIdFromUserEvaluated() {
        return idFromUserEvaluated;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }
}
