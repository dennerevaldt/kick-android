package br.unisinos.kickoffapp.models;

import java.io.Serializable;

/**
 * Created by dennerevaldtmachado on 09/07/16.
 */
public class Place implements Serializable {
    /**
     * atributtes
     */
    private String idPlace;
    private String description;

    /**
     * constructor
     * @param idPlace
     * @param description
     */
    public Place(String idPlace, String description) {
        this.idPlace = idPlace;
        this.description = description;
    }

    /**
     * getters and setter
     * @return
     */
    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
