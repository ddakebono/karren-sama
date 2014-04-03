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

public class HueCommand extends ListenerAdapter<PircBotX>{
    private static int hueCount = 0;
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		KarrenBot bot = (KarrenBot)event.getBot();
		String cmd = "Hue";
		String message = event.getMessage();
		if(message.toLowerCase().startsWith(cmd.toLowerCase())){
			event.getChannel().send().message("Hue");
			hueCount++;
			if(hueCount >= 3){
				if(event.getChannel().isOp(bot.getUserBot())){
					event.getChannel().send().kick(event.getUser(), "Hue hue hue!");
					event.getChannel().send().message("Wow, " + event.getUser().getNick() + " just got #rekt.");
					bot.getLog().info("Karren-sama has kicked " + event.getUser().getNick() + "For Overhue");
				} else {
					event.respond("You're lucky this time, I don't have permission to kick you right now...");
					bot.getLog().error("Couldn't kick " + event.getUser().getNick() + " because permission are missing!");
				}
				hueCount = 0;
			}
		}
	}

}
