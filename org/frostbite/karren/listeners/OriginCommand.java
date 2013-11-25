package org.frostbite.karren.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class OriginCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".origin";
		PircBotX bot = event.getBot();
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			event.respond("Wow Origin is fucking terrible, you should probably just kill yourself.");
		}
	}

}
