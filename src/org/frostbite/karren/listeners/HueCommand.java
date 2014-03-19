/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Logging;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HueCommand extends ListenerAdapter<PircBotX>{
    private static int hueCount = 0;
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		PircBotX bot = event.getBot();
		String cmd = "Hue";
		String message = event.getMessage();
		if(message.toLowerCase().startsWith(cmd.toLowerCase())){
			event.getChannel().send().message("Hue");
			hueCount++;
			if(hueCount >= 3){
				if(event.getChannel().isOp(bot.getUserBot())){
					event.getChannel().send().kick(event.getUser(), "Hue hue hue!");
					event.getChannel().send().message("Wow, " + event.getUser().getNick() + " just got #rekt.");
					Logging.log("Karren-sama has kicked " + event.getUser().getNick() + "For Overhue", false);
				} else {
					event.respond("You're lucky this time, I don't have permission to kick you right now...");
					Logging.log("Couldn't kick " + event.getUser().getNick() + " because permission are missing!", true);
				}
				hueCount = 0;
			}
		}
	}

}
