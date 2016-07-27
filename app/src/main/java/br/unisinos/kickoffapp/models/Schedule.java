package br.unisinos.kickoffapp.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class Schedule implements Serializable {
    /**
     * attributes
     */
    private String idSchedule;
    private String horary;
    private String date;
    private Court court;

    /**
     * constructor
     * @param idSchedule
     * @param horary
     * @param date
     * @param court
     */
    public Schedule(String idSchedule, String horary, String date, Court court) {
        this.idSchedule = idSchedule;
        this.horary = horary;
        this.date = date;
        this.court = court;
    }

    /**
     * getters and setter
     */
    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getHorary() {
        return horary;
    }

    public void setHorary(String horary) {
        this.horary = horary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public String getDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(dt);
    }

    public String getDateFormatMySql() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(dt);
    }

    public String getHoraryFormat() {
        String hour = horary.split(":")[0];
        String min = horary.split(":")[1];
        return hour+":"+min;
    }

    public int getDay() {
        return Integer.parseInt(getDateFormat().split("/")[0]);
    }

    public int getMonth() {
        return Integer.parseInt(getDateFormat().split("/")[1]);
    }

    public int getYear() {
        return Integer.parseInt(getDateFormat().split("/")[2]);
    }

    public int getHour() {
        return Integer.parseInt(getHoraryFormat().split(":")[0]);
    }

    public int getMinutes() {
        return Integer.parseInt(getHoraryFormat().split(":")[1]);
    }
}
