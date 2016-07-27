package br.unisinos.kickoffapp.models;

import java.io.Serializable;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class Court implements Serializable {
    /**
     * attributes
     */
    private String idCourt;
    private String name;
    private String category;

    /**
     * constructor
     * @param idCourt
     * @param name
     * @param category
     */
    public Court(String idCourt, String name, String category) {
        this.idCourt = idCourt;
        this.name = name;
        this.category = category;
    }

    /**
     * getters and setters
     */
    public String getIdCourt() {
        return idCourt;
    }

    public void setIdCourt(String idCourt) {
        this.idCourt = idCourt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
