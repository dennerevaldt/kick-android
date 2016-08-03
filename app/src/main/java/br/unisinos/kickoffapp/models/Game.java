package br.unisinos.kickoffapp.models;

import java.util.List;

/**
 * Created by dennerevaldtmachado on 28/07/16.
 */
public class Game {

    /**
     * attributes
     */
    private String id;
    private String name;
    private String creator_id;
    private Schedule schedule;
    private List<Player> playerList;
    private Court court;

    /**
     * constructor empty
     */
    public Game(){}

    /**
     * constructor complete
     * @param id
     * @param name
     * @param creator_id
     * @param schedule
     * @param playerList
     */
    public Game(String id, String name, String creator_id, Schedule schedule, List<Player> playerList, Court court) {
        this.id = id;
        this.name = name;
        this.creator_id = creator_id;
        this.schedule = schedule;
        this.playerList = playerList;
        this.court = court;
    }

    /**
     * getters and setters
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }
}
