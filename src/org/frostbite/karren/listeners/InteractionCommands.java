/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class InteractionCommands extends ListenerAdapter<PircBotX> {
    public void onMessage(MessageEvent<PircBotX> event){
        String msg = event.getMessage();
        String returned = "";
        String[] tags;
        boolean hasBotTag = false;
        String[] data = new String[1];
        KarrenBot bot = (KarrenBot)event.getBot();
        ArrayList<Object> resultData = new ArrayList<>();
        if(bot.getBotConf().getEnableInteractions().equalsIgnoreCase("true")){
            for(Interactions check : bot.getInteractions()){
                returned = check.handleMessage(event);
                if(returned.length()>0){
                    tags = check.getTags();
                    for(String tag : tags){
                        switch (tag.toLowerCase()){
                            case "name":
                                returned = returned.replace("%name", event.getUser().getNick());
                                break;
                            case "depart":
                                data[0] = event.getUser().getNick();
                                try {
                                    resultData.addAll(bot.getSql().getUserData(event.getUser().getNick()));
                                    if((boolean)resultData.get(1)){
                                        returned = "Wait " + event.getUser().getNick() + ", you left earlier...well fine, good bye again.";
                                    }
                                    bot.getSql().userOperation("part", data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "song":
                                returned = returned.replace("%song", bot.getListenCast().getSong().getSongName());
                                break;
                            case "version":
                                returned = returned.replace("%version", bot.getBotConf().getVersionMarker());
                                break;
                            case "songtime":
                                returned = returned.replace("%songtime", bot.getListenCast().getMinSecFormattedString(bot.getListenCast().getSongCurTime()));
                                break;
                            case "songdur":
                                returned = returned.replace("%songdur", bot.getListenCast().getMinSecFormattedString(bot.getListenCast().getSong().getLastSongDuration()));
                                break;
                            case "dj":
                                returned = returned.replace("%dj", bot.getListenCast().getIceDJ());
                                break;
                            case "random":
                                String[] tempArray = event.getMessage().split(":");
                                if(tempArray.length==2){
                                    returned = returned.replace("%result", randomList(tempArray[1]));
                                } else {
                                    returned = "You want me to pick something but I can't tell what anything is...";
                                }
                                break;
                            case "bot":
                                hasBotTag = true;
                                break;
                            case "return":
                                data[0] = event.getUser().getNick();
                                try {
                                    resultData.addAll(bot.getSql().getUserData(event.getUser().getNick()));
                                    bot.getSql().userOperation("return", data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if((boolean)resultData.get(1)){
                                    returned = returned.replace("%away", calcAway((long)resultData.get(2)));
                                } else {
                                    returned = "Hey," + event.getUser().getNick() + " are you new? Be sure to say good bye to me when you leave!";
                                }
                                break;
                            default:
                                bot.getLog().error("Please check the Interactions file, a tag that does not exist is being used!");
                        }
                    }
                    break;
                }
            }
            if(hasBotTag && msg.toLowerCase().contains(bot.getBotConf().getBotname().toLowerCase()))
                event.getChannel().send().message(returned);
            else if(msg.toLowerCase().contains(bot.getBotConf().getBotname().toLowerCase()) && returned.length()==0)
                event.respond("It's not like I wanted to answer anyways....baka. (Use \"" + bot.getBotConf().getCommandPrefix() + "help interactions\" to view all usable interactions)");
            else if(returned.length()>0 && !hasBotTag)
                event.getChannel().send().message(returned);

        }
    }
    public static String randomList(String message){
        String[] choiceSet = message.split(",");
        String choice;
        int random = new Random().nextInt(choiceSet.length);
        choice = choiceSet[random].trim();
        return choice;
    }

    public static String calcAway(long leaveDate){
        String backTime;
        long diffTime;
        long seconds;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = date.getTime()-leaveDate;
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
