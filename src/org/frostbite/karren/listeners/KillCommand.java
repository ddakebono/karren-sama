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

public class KillCommand implements IListener<MessageReceivedEvent> {
	public void handle(MessageReceivedEvent event){
		String message = event.getMessage().getContent();
		IDiscordClient bot = event.getClient();
        String cmd = Karren.conf.getCommandPrefix() + "kill";
		if((isAdmin(event.getMessage().getAuthor(), bot)) && message.toLowerCase().startsWith(cmd)){
            Karren.bot.killBot(event.getMessage().getAuthor().getName());
        } else {
			if(!isAdmin(event.getMessage().getAuthor(), bot) && message.startsWith(cmd))
                try {
                    event.getMessage().getChannel().sendMessage("You can't tell me what to do! (Not Admin)");
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
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
