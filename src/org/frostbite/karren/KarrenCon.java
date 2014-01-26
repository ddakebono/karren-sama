/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.pircbotx.Channel;
import org.pircbotx.User;

import java.sql.Time;
import java.util.Date;

/**
 * Created by frostbite on 1/21/14.
 */
public class KarrenCon {
    private int id;
    private User userName;
    private Channel userChan;
    private boolean dontFaveAlert;
    private boolean isBotParted;
    private long timeOfPart;
    private boolean ignoreUser;
    private long timeWasted;
    private long userEntryTimestamp;
    public KarrenCon(User user, Channel channel, long entryTime, boolean isBotParted, long timeParted, long timeWasted, boolean isIgnored, boolean faveSetting){
        this.userName = user;
        this.userChan = channel;
        this.userEntryTimestamp = entryTime;
        this.isBotParted = isBotParted;
        this.timeOfPart = timeParted;
        this.timeWasted = timeWasted;
        this.ignoreUser = isIgnored;
    }
    public String toString(){
        String result = userName.getNick() + " " + userChan.getName();
        return result;
    }
    public int getId(){
        return id;
    }
    public void setIgnore(boolean set){
        Logging.log(userName.getNick() + " Added to ignore list for spam!", false);
        ignoreUser = set;
    }
    public long getUserLoggedInTime(){
        long result = 0;
        Date date = new Date();
        result = date.getTime() - userEntryTimestamp;
        return result;
    }
    public User getUserObject(){
        return userName;
    }
    public void setFaveAlert(boolean set){
        dontFaveAlert = set;
    }
    public boolean getFaveAlert(){
        return dontFaveAlert;
    }
    public boolean getParted(){return isBotParted;}
    public boolean getIgnored(){
        return ignoreUser;
    }
    public long getUserEntryTimestamp(){ return userEntryTimestamp;}
    public long getTimeOfPart(){return timeOfPart;}
    public long getTimeWasted(){return timeWasted;}
    public String getUserName(){ return userName.getNick();}
}