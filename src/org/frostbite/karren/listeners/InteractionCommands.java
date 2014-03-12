/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Interactions;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by frostbite on 2/4/14.
 */
public class InteractionCommands extends ListenerAdapter {
    public void onMessage(MessageEvent event){
        String msg = event.getMessage();
        String returned = "";
        String[] tags;
        Object[] data = new Object[1];
        ArrayList<Object> resultData = new ArrayList<Object>();
        Pattern match = Pattern.compile(".*" + GlobalVars.botname + ".*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        if(msg.toLowerCase().contains(GlobalVars.botname.toLowerCase())){
            for(Interactions check : GlobalVars.interactions){
                returned = check.handleMessage(event);
                if(returned.length()!=0){
                    tags = check.getTags();
                    for(String tag : tags){
                        switch (tag.toLowerCase()){
                            case "name":
                                returned.replace("%name", event.getUser().getNick());
                                break;
                            case "depart":
                                data[0] = event.getUser().getNick();
                                try {
                                    MySQLConnector.sqlPush("part", "", data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                GlobalVars.awayUser.add(event.getUser().getNick());
                                break;
                            case "random":
                                String[] tempArray = event.getMessage().split(":");
                                if(tempArray.length==2){
                                    returned = check.getResponseTemplate();
                                    returned.replace("%result", randomList(tempArray[1]));
                                } else {
                                    returned = "You want me to pick something but I can't tell what anything is...";
                                }
                                break;
                            case "return":
                                data[0] = event.getUser().getNick();
                                try {
                                    resultData.addAll(MySQLConnector.sqlPush("part", "back", data));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if(resultData.get(0)!=null){
                                    returned.replace("%away", calcAway((String)resultData.get(0)));
                                } else {
                                    returned = "Hey," + event.getUser().getNick() + " are you bad? Say good bye next time!";
                                }
                        }
                    }
                }
            }
            if(returned.length()!=0){
                event.getChannel().send().message(returned);
            } else {
                event.respond("It's not like I wanted to answer anyways....baka.");
            }
        }
    }
    public static String randomList(String message){
        String[] choiceSet = message.split("[\\s*],[\\s*]");
        String choice = "";
        int random = new Random().nextInt(choiceSet.length);
        choice = choiceSet[random];
        return choice;
    }
    public static String calcAway(String leaveDate){
        String backTime = "0";
        long diffTime = 0;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = date.getTime()-Long.parseLong(leaveDate);
        seconds = diffTime/1000;
        if(seconds>=60){
            minutes = seconds/60;
            seconds = seconds-(minutes*60);
        }
        if(minutes>=60){
            hours = minutes/60;
            minutes = minutes-(hours*60);
        }
        if(hours>=24){
            days = hours/24;
            hours = hours-(days*24);
        }
        backTime = days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds.";
        return backTime;
    }
}
