package br.unisinos.kickoffapp.models;

import java.io.Serializable;

/**
 * Created by dennerevaldtmachado on 25/06/16.
 */
public class Player extends Person implements Serializable {
    /**
     * attributes
     */
    private int idPlayer;
    private String position;

    /**
     * constructor
     */
    public Player(int idPerson, String fullName, String userName, String eMail, String password, String district, String lat, String lng, int idPlayer, String position) {
        super(idPerson, fullName, userName, eMail, password, district, lat, lng);
        this.idPlayer = idPlayer;
        this.position = position;
    }

    /**
     * getters and setters
     */
    public int getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
