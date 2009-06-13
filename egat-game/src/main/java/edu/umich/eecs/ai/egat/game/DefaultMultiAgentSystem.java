/*
 * DefaultMultiAgentSystem.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class DefaultMultiAgentSystem implements MutableMultiAgentSystem, Cloneable {
    private String name;
    private String description;
    private Set<Player> players;


    public DefaultMultiAgentSystem() {
        players = new HashSet<Player>();
    }


    public DefaultMultiAgentSystem(String name) {
        this.name = name;
        players = new HashSet<Player>();
    }


    public DefaultMultiAgentSystem(String name, String description) {
        this.name = name;
        this.description = description;
        players = new HashSet<Player>();
    }


    public DefaultMultiAgentSystem(String name, String description, Set<Player> players) {
        this.name = name;
        this.description = description;
        // We may want to consider a defensive copy here.
        this.players = players;
    }


    public DefaultMultiAgentSystem(Set<Player> players) {
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
        beforeAddPlayer(player);

        boolean status = players.add(player);

        if(status) {
            afterAddPlayer(player);
        }

        return status;
    }

    protected void afterAddPlayer(Player player) {
    }

    protected void beforeAddPlayer(Player player) {
    }

    public boolean removePlayer(Player player) {
        beforeRemovePlayer(player);
        boolean status = players.remove(player);

        if(status) {
            afterRemovePlayer(player);
        }

        return status;
    }

    protected void beforeRemovePlayer(Player player) {
    }

    protected void afterRemovePlayer(Player player) {        
    }

    @Override
    protected DefaultMultiAgentSystem clone() throws CloneNotSupportedException {
        DefaultMultiAgentSystem mas = (DefaultMultiAgentSystem) super.clone();
        mas.players = new HashSet<Player>(players);

        return mas;    
    }
}
