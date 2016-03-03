/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.HTTP429Exception;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MiscCommands implements IListener<MessageReceivedEvent> {
	public void handle(MessageReceivedEvent event){
		String[] cmds = {"echo", "isgay", "npswitch", "reloadint", "fave", "reloadserv", "recover-nick"};
		String message = event.getMessage().getContent();
		String cmd = "";
        IDiscordClient bot = event.getClient();
		if(message.startsWith(Karren.conf.getCommandPrefix())){
			message = message.replaceFirst(Pattern.quote(Karren.conf.getCommandPrefix()), "").trim();
			for(String check : cmds){
				if(message.toLowerCase().startsWith(check)){
					cmd = check;
					message = message.replaceFirst("(?i)" + check, "").trim();
				}
			}
			switch(cmd) {
                case "echo":
                    try {
                        event.getMessage().getChannel().sendMessage(message.trim());
                    } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                        e.printStackTrace();
                    }
                    break;
                case "isgay":
                    try {
                        event.getMessage().getChannel().sendMessage("Wow, " + message.trim() + " is so fucking gaaaaaaaaay!");
                    } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                        e.printStackTrace();
                    }
                    break;
                case "fave":
                    if(Karren.bot.getListenCast().getSong().getSongID()!=0) {
                        try {
                            bot.getOrCreatePMChannel(event.getMessage().getAuthor()).sendMessage(Karren.bot.getListenCast().getSong().getSongName() + " added to your favorites!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Karren.bot.getSql().addFave(event.getMessage().getAuthor().getID(), Karren.bot.getListenCast().getSong());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bot.getOrCreatePMChannel(event.getMessage().getAuthor()).sendMessage("You can't fave something when noone is streaming!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "reloadint":
                    if(Karren.conf.getEnableInteractions().equalsIgnoreCase("true")) {
                        if (isAdmin(event.getMessage().getAuthor(), bot)) {
                            Karren.log.info("Interactions system reload triggered by " + event.getMessage().getAuthor().getName());
                            Karren.bot.reloadInteractions();
                        } else {
                            try {
                                event.getMessage().getChannel().sendMessage("You do not have the permission to use this... (Not Admin)");
                            } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            event.getMessage().getChannel().sendMessage("Interations system disabled by configuration");
                        } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "npswitch":
                    if (isAdmin(event.getMessage().getAuthor(), bot)) {
                        if (Karren.bot.getListenCast().enableNP()) {
                            try {
                                event.getMessage().getChannel().sendMessage("Automagic now playing has been activated!");
                            } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                event.getMessage().getChannel().sendMessage("Automagic now playing has been deactivated...");
                            } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            event.getMessage().getChannel().sendMessage("You do not have the permissions to change this... (Not Admin)");
                        } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
			}
		}
	}
    boolean isAdmin(IUser user, IDiscordClient bot){
        boolean result = false;
        for(IRole role : user.getRolesForGuild(bot.getGuildByID(Karren.conf.getGuildId()))){
            if(role.getName().equals("Admins")){
                result = true;
            }
        }
        return result;
    }
}
