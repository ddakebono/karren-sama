package org.frostbite.karren.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class GayCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String message = event.getMessage();
		String cmd = ".isgay";
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			event.getChannel().send().message("Wow, " + message + " is so fucking gaaaaaaaaay!");
		}
	}

}