/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MiscCommands extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event){
		String[] cmds = {"echo", "isgay", "npswitch", "reloadint", "fave", "reloadserv", "recover-nick"};
		String message = event.getMessage();
		String cmd = "";
        KarrenBot bot = (KarrenBot)event.getBot();
		if(message.startsWith(bot.getBotConf().getCommandPrefix())){
			message = message.replaceFirst(Pattern.quote(bot.getBotConf().getCommandPrefix()), "").trim();
			for(String check : cmds){
				if(message.toLowerCase().startsWith(check)){
					cmd = check;
					message = message.replaceFirst("(?i)" + check, "").trim();
				}
			}
			switch(cmd) {
                case "echo":
                    event.respond(message.trim());
                    break;
                case "isgay":
                    event.getChannel().send().message("Wow, " + message.trim() + " is so fucking gaaaaaaaaay!");
                    break;
                case "fave":
                    if(bot.getListenCast().getSong().getSongID()!=0) {
                        event.getUser().send().message(bot.getListenCast().getSong().getSongName() + " added to your favorites!");
                        try {
                            bot.getSql().addFave(event.getUser().getNick(), bot.getListenCast().getSong());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        event.getUser().send().message("You can't fave something that isn't playing!");
                    }
                    break;
                case "reloadint":
                    if(bot.getBotConf().getEnableInteractions().equalsIgnoreCase("true")) {
                        if (event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())) {
                            bot.getLog().info("Interactions system reload triggered by " + event.getUser().getNick());
                            bot.reloadInteractions();
                        } else {
                            event.respond("You do not have the permissions to use this...(Not Operator)");
                        }
                    } else {
                        event.respond("Interactions system disabled by configuration.");
                    }
                    break;

                case "npswitch":
                    if (event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())) {
                        if (bot.getListenCast().enableNP()) {
                            event.getChannel().send().message("Automagic now playing has been activated!");
                        } else {
                            event.getChannel().send().message("Automagic now playing has been deactivated...");
                        }
                    } else {
                        event.respond("You do not have the permissions to change this...(Not Operator)");
                    }
                    break;
                case "recover-nick":
                    if(event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser())){
                        if(!bot.recoverNick())
                            event.respond("I already have the nick I was told to have, if you want to change it check the configuration.");
                    } else {
                        event.respond("You do not have the permissions required to use this... (Operator/Owner required)");
                    }
                    break;
			}
		}
	}
}
