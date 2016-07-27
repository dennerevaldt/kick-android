package br.unisinos.kickoffapp.models;

import java.io.Serializable;

/**
 * Created by dennerevaldtmachado on 25/06/16.
 */
public class Enterprise extends Person implements Serializable {
    /**
     * attributes
     */
    private int idEnterprise;
    private String telephone;

    /**
     * constructor
     */
    public Enterprise(int idPerson, String fullName, String userName, String eMail, String password, String district, String lat, String lng, int idEnterprise, String telephone) {
        super(idPerson, fullName, userName, eMail, password, district, lat, lng);
        this.idEnterprise = idEnterprise;
        this.telephone = telephone;
    }

    /**
     * getters and setters
     */
    public int getIdEnterprise() {
        return idEnterprise;
    }

    public void setIdEnterprise(int idEnterprise) {
        this.idEnterprise = idEnterprise;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
