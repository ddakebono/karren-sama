package org.frostbite.karren.space;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.MySQLInterface;
import org.pircbotx.Channel;

import java.sql.SQLException;
import java.util.Date;

public class SpaceController extends Thread {
    private MySQLInterface sql;
    private SpaceFaction[] factions;
    private SpaceEvent[] events;
    private SpaceUser[] users;
    private boolean stopController;
    private KarrenBot bot;
    private Channel announceChannel;
    public SpaceController(MySQLInterface sql, KarrenBot bot, Channel channel){
        this.sql = sql;
        this.announceChannel = channel;
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
    public void killController(){
        stopController = true;
    }
    public void run(){
        Date date = new Date();
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
