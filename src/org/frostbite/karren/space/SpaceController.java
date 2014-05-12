package org.frostbite.karren.space;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.MySQLInterface;
import org.pircbotx.Channel;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Date;

public class SpaceController extends Thread {
    private MySQLInterface sql;
    private SpaceFaction[] factions;
    private SpaceEvent[] events;
    private SpaceUser[] users;
    private boolean stopController;
    private KarrenBot bot;
    private Logger log;
    private Channel announceChannel;
    public SpaceController(MySQLInterface sql, KarrenBot bot){
        this.sql = sql;
        this.bot = bot;
        log = bot.getLog();
        loadFromSQL();
    }
    public void loadFromSQL(){
        try {
            log.info("Loading all data for Space Engineers tracker.");
            factions = sql.loadSpaceFactions();
            events = sql.loadSpaceEvents();
            users = sql.loadSpaceUsers();
            log.debug("Space Engineers data loaded!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void killController() throws SQLException {
        log.info("Shutting down and save Space Engineers Data...");
        stopController = true;
        saveAllData();
        log.info("Space Engineers Controller data saved!");
    }
    public void saveAllData() throws SQLException {
        for(SpaceEvent event : events)
            sql.saveSpaceEvent(event);
        for(SpaceUser user : users)
            sql.saveSpaceUser(user);
        for(SpaceFaction faction : factions)
            sql.saveSpaceFaction(faction);
    }
    public void addUser(SpaceUser user) throws SQLException {
        for(SpaceUser saveUser : users)
            sql.saveSpaceUser(saveUser);
        sql.saveSpaceUser(user);
        users = sql.loadSpaceUsers();
    }
    public void addFaction(SpaceFaction faction) throws SQLException {
        for(SpaceFaction saveFaction : factions)
            sql.saveSpaceFaction(saveFaction);
        sql.saveSpaceFaction(faction);
        factions = sql.loadSpaceFactions();
    }
    public void addEvent(SpaceEvent event) throws SQLException{
        for(SpaceEvent saveEvent : events)
            sql.saveSpaceEvent(saveEvent);
        sql.saveSpaceEvent(event);
        events = sql.loadSpaceEvents();
    }
    public SpaceUser[] getUsers(){
        return users;
    }
    public SpaceFaction[] getFactions(){return factions;}
    public void run(){
        stopController = false;
        Date date = new Date();
        announceChannel = bot.getUserBot().getChannels().first();
        while(!stopController){
            for(SpaceEvent event : events){
                if(event.getStartDate() <= date.getTime() && !event.hasEventBegun()){
                    announceChannel.send().message(event.getEventDataFormatted());
                }
            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
