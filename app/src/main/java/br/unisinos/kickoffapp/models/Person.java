package br.unisinos.kickoffapp.models;

import java.io.Serializable;

/**
 * Created by dennerevaldtmachado on 25/06/16.
 */
public abstract class Person implements Serializable {
    /**
     * attributes
     */
    private int idPerson;
    private String fullName;
    private String userName;
    private String eMail;
    private String password;
    private String district;
    private String lat;
    private String lng;

    /**
     * constructor
     */
    public Person(int idPerson, String fullName, String userName, String eMail, String password, String district, String lat, String lng) {
        this.idPerson = idPerson;
        this.fullName = fullName;
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.district = district;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * getters and setters
     */
    public int getId() {
        return idPerson;
    }

    public void setId(int id) {
        this.idPerson = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
