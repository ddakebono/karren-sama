package org.frostbite.karren.space;

import org.frostbite.karren.MySQLInterface;

import java.sql.SQLException;

public class SpaceController {
    private MySQLInterface sql;
    private SpaceFaction[] factions;
    private SpaceEvent[] events;
    public SpaceController(MySQLInterface sql){
        this.sql = sql;
        loadAllFactions();
        loadAllEvents();
    }
    public void loadAllFactions(){
        try {
            factions = sql.loadSpaceFactions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadAllEvents(){
        try {
            events = sql.loadSpaceEvents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
