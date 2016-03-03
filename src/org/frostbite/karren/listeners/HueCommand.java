/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.HTTP429Exception;

public class HueCommand implements IListener<MessageReceivedEvent>{
    private static int hueCount = 0;
	public void handle(MessageReceivedEvent event) {
		String cmd = "Hue";
		String message = event.getMessage().getContent();
		if(message.toLowerCase().startsWith(cmd.toLowerCase())){
			try {
				event.getMessage().getChannel().sendMessage("Hue");
			} catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
				e.printStackTrace();
			}
			hueCount++;
			if(hueCount >= 3){
				try {
                    event.getMessage().getChannel().sendMessage("You're lucky this time, I don't have permission to kick you right now...");
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
				Karren.log.error("Couldn't kick " + event.getMessage().getAuthor().getName() + " because permission are missing!");
				hueCount = 0;
			}
		}
	}
}
