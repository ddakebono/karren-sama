package org.frostbite.karren.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class BradCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".brad";
		if(message.startsWith(cmd)){
			event.respond("poop");
		}
	}

}
