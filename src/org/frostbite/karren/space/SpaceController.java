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
            factions = sql.loadSpaceFactions();
            events = sql.loadSpaceEvents();
            users = sql.loadSpaceUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void killController() throws SQLException {
        log.info("Shutting down and save Space Engineers Data...");
        stopController = true;
        for(SpaceEvent event : events)
            sql.saveSpaceEvent(event);
        for(SpaceUser user : users)
            sql.saveSpaceUser(user);
        for(SpaceFaction faction : factions)
            sql.saveSpaceFaction(faction);
        log.info("Space Engineers Controller data saved!");
    }
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
