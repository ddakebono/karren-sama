/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Interactions;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class InteractionCommands implements IListener<MessageReceivedEvent>{
    public void handle(MessageReceivedEvent event){
        String msg = event.getMessage().getContent();
        String returned = null;
        String[] tags;
        boolean hasBotTag = false;
        boolean privateMessage = false;
        String[] data = new String[1];
        ArrayList<Object> resultData = new ArrayList<>();
        if(Karren.conf.getEnableInteractions().equalsIgnoreCase("true")){
            for(Interactions check : Karren.bot.getInteractions()){
                returned = check.handleMessage(event);
                if(returned!=null){
                    tags = check.getTags();
                    for(String tag : tags){
                        switch (tag.toLowerCase()){
                            case "echo":
                                //Echo is limited by this code, only single trigger command will work (Ex. .echo Test)
                                returned = returned.replace("%echo", event.getMessage().getContent().replace( Karren.conf.getCommandPrefix() + check.getTriggers()[0], "").trim());
                                break;
                            case "name":
                                returned = returned.replace("%name", event.getMessage().getAuthor().getName());
                                break;
                            case "depart":
                                data[0] = event.getMessage().getAuthor().getID();
                                try {
                                    resultData.addAll(Karren.bot.getSql().getUserData(event.getMessage().getAuthor().getID()));
                                    if(Boolean.parseBoolean(String.valueOf(resultData.get(2)))){
                                        returned = "Wait " + event.getMessage().getAuthor().getName() + ", you left earlier...well fine, good bye again.";
                                    }
                                    Karren.bot.getSql().userOperation("part", data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "fave":
                                if(Karren.bot.getListenCast().getSong().getSongID()!=0) {
                                    try {
                                        Karren.bot.getSql().addFave(event.getMessage().getAuthor().getID(), Karren.bot.getListenCast().getSong());
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    returned = check.getRandomTemplatesFail();
                                }
                                break;
                            case "song":
                                returned = returned.replace("%song", Karren.bot.getListenCast().getSong().getSongName());
                                break;
                            case "version":
                                returned = returned.replace("%version", Karren.conf.getVersionMarker());
                                break;
                            case "songtime":
                                returned = returned.replace("%songtime", Karren.bot.getListenCast().getMinSecFormattedString(Karren.bot.getListenCast().getSongCurTime()));
                                break;
                            case "songdur":
                                returned = returned.replace("%songdur", Karren.bot.getListenCast().getMinSecFormattedString(Karren.bot.getListenCast().getSong().getLastSongDuration()));
                                break;
                            case "dj":
                                returned = returned.replace("%dj", Karren.bot.getListenCast().getIceDJ());
                                break;
                            case "random":
                                String[] tempArray = event.getMessage().getContent().split(":");
                                if(tempArray.length==2){
                                    returned = returned.replace("%result", randomList(tempArray[1]));
                                } else {
                                    returned = "You want me to pick something but I can't tell what anything is...";
                                }
                                break;
                            case "bot":
                                hasBotTag = true;
                                break;
                            case "pm":
                                privateMessage = true;
                                break;
                            case "return":
                                data[0] = event.getMessage().getAuthor().getID();
                                try {
                                    resultData.addAll(Karren.bot.getSql().getUserData(event.getMessage().getAuthor().getID()));
                                    Karren.bot.getSql().userOperation("return", data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if(Boolean.parseBoolean(String.valueOf(resultData.get(2)))){
                                    returned = returned.replace("%away", calcAway((long)resultData.get(3)));
                                } else {
                                    returned = "Hey," + event.getMessage().getAuthor().getName() + " are you new? Be sure to say good bye to me when you leave!";
                                }
                                break;
                            case "prefixed":
                                break;
                            default:
                                Karren.log.error("Please check the Interaction file for " + check.getIdentifier() + ", a tag that does not exist is being used!");
                        }
                    }
                    break;
                }
            }
            if(hasBotTag && msg.toLowerCase().contains(Karren.bot.getClient().getOurUser().getName().toLowerCase()))
                try {
                    if(!privateMessage)
                        event.getMessage().getChannel().sendMessage(returned);
                    else
                        Karren.bot.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()).sendMessage(returned);
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
            else if(msg.toLowerCase().contains(Karren.bot.getClient().getOurUser().getName().toLowerCase()) && returned==null) {
                try {
                    event.getMessage().getChannel().sendMessage("It's not like I wanted to answer anyways....baka. (Use \"" + Karren.conf.getCommandPrefix() + "help interactions\" to view all usable interactions)");
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
            }
            else if(returned!=null && !hasBotTag)
                try {
                    if(!privateMessage)
                        event.getMessage().getChannel().sendMessage(returned);
                    else
                        Karren.bot.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()).sendMessage(returned);
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }

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
