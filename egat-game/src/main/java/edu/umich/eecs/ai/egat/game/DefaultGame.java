package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class DefaultGame implements Game {
    private String name;
    private String description;
    private Set<Player> players;


    public DefaultGame() {
        players = new HashSet<Player>();
    }


    public DefaultGame(String name) {
        this.name = name;
        players = new HashSet<Player>();
    }


    public DefaultGame(String name, String description) {
        this.name = name;
        this.description = description;
        players = new HashSet<Player>();
    }


    public DefaultGame(String name, String description, Set<Player> players) {
        this.name = name;
        this.description = description;
        // We may want to consider a defensive copy here.
        this.players = players;
    }


    public DefaultGame(Set<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<Player> players() {
        return players;
    }

    public boolean addPlayer(Player player) {
        return players.add(player);
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }
}
