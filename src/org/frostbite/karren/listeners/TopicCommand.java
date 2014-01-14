/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Logging;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TopicCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		String message = event.getMessage();
		String cmd = ".topic";
		PircBotX bot = event.getBot();
		Channel channel = event.getChannel();
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			if(channel.isOp(event.getUser()) || channel.hasVoice(event.getUser())){
				if(message != ""){
					if(event.getChannel().isOp(bot.getUserBot())){
						channel.send().setTopic(Colors.RED + "CRaZyPANTS Minecraft channel" + Colors.BLACK + ", use .help to get current commands avalable for use. MOTD: " + (Colors.GREEN + message));
						Logging.log("The MOTD has been changed to " + message + " By " + event.getUser().getNick(), false);
					} else {
						event.getChannel().send().message("I don't seem to have the correct permissions to change the topic...");
						Logging.log("Error! Couldn't set topic because permissions are missing!", true);
					}
				} else {
					event.respond("Your message does not contain a new MOTD");
				}
			} else {
				event.respond("You do not have permission to change the topic!");
			}
		}
	}

}
