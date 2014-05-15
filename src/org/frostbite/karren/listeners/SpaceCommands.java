package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.space.SpaceFaction;
import org.frostbite.karren.space.SpaceUser;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;

public class SpaceCommands extends ListenerAdapter<PircBotX>{
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        String msg = event.getMessage();
        if(msg.toLowerCase().startsWith(bot.getBotConf().getCommandPrefix() + "space")){
            msg = msg.substring(bot.getBotConf().getCommandPrefix().length() + 5).trim();
            if(msg.toLowerCase().startsWith("register")){
                msg = msg.substring(8).trim();
                if(msg.length()>0 && msg.contains("@")) {
                    if (findUserInUsers(event.getUser().getNick(), bot.getSpace().getUsers()) == -1) {
                        try {
                            bot.getSpace().addUser(new SpaceUser(event.getUser().getNick(), 0, 0, msg));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        event.respond("You are now added to the Space Engineers user tracker, events related to you or your faction will be displayed.");
                    } else {
                        event.respond("You have already registered with the Space Engineers tracker.");
                    }
                } else {
                    event.respond("You must supply a real email when adding an account to the Space Engineers tracker!");
                }
            } else if(msg.toLowerCase().startsWith("faction")){
                SpaceUser[] users = bot.getSpace().getUsers();
                int userPos = findUserInUsers(event.getUser().getNick(), users);
                msg = msg.substring(7).trim();
                if(userPos!=-1){
                    if(msg.length()>0){
                        users[userPos].setFactionID(getFactionIDFromName(msg, bot.getSpace().getFactions()));
                        if(users[userPos].getFactionID()>0)
                            event.respond("You have joined the faction " + getFactionFromID(users[userPos].getFactionID(), bot.getSpace().getFactions()).getFactionName() + "!" );
                        else
                            event.respond("The faction you attempted to join doesn't exist.");
                    } else {
                        SpaceFaction faction = getFactionFromID(users[userPos].getFactionID(), bot.getSpace().getFactions());
                        if(faction != null){
                            event.respond("You are currently in the faction " + faction.getFactionName());
                        } else {
                            event.respond("You are not currently in a faction.");
                        }
                    }
                } else {
                    event.respond("You have not registered with the Space Engineers tracker, please do so before accessing faction data.");
                }
            } else if(msg.toLowerCase().startsWith("add")) {
                if (event.getChannel().hasVoice(event.getUser()) || event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())) {
                    msg = msg.substring(3).trim();
                    if (msg.toLowerCase().startsWith("faction")) {
                        msg = msg.substring(7).trim();
                        if (msg.length() > 0) {
                            SpaceFaction[] factions = bot.getSpace().getFactions();
                            if (getFactionIDFromName(msg, factions) == -1) {
                                try {
                                    bot.getSpace().addFaction(new SpaceFaction(0, msg));
                                    event.respond("Faction " + msg + " has been created, due to a how factions are handled you must manually join the faction.");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                event.respond("A faction with that name already exists.");
                            }
                        } else {
                            event.respond("You must give a name to create a faction.");
                        }
                    } else if (msg.toLowerCase().startsWith("event")) {
                        event.respond("The event system is currently disabled!");
                    } else {
                        event.respond("You have to tell me what you want to add.");
                    }
                } else {
                    event.respond("You do not have permission to add data to the Space Engineers tracker.");
                }
            } else if(msg.toLowerCase().startsWith("save")){
                if(event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())) {
                    try {
                        bot.getSpace().saveAllData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    event.respond("You do not have permission to force save the Space Engineers data.");
                }
            } else {
                event.respond("Yay space! (Check the help to find out what this does)");
            }
        }
    }
    public int findUserInUsers(String nick, SpaceUser[] users){
        int result = -1;
        for(int i=0; i<users.length; i++){
            if(users[i].getNick().equalsIgnoreCase(nick)){
                result = i;
                break;
            }
        }
        return result;
    }
    public int getFactionIDFromName(String name, SpaceFaction[] factions){
        int result = -1;
        for(int i=0; i<factions.length; i++){
            if(factions[i].getFactionName().equalsIgnoreCase(name)){
                result = i+1;
                break;
            }
        }
        return result;
    }
    public SpaceFaction getFactionFromID(int id, SpaceFaction[] factions){
        SpaceFaction result = null;
        for(SpaceFaction faction : factions){
            if(faction.getFactionID() == id){
                result = faction;
                break;
            }
        }
        return result;
    }
}
