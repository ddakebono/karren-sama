package org.frostbite.karren.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;

public class TerminateBot extends ListenerAdapter{
	public void onPart(PartEvent event) throws Exception{
		PircBotX bot = event.getBot();
		if(event.getUser().getNick() == bot.getNick()){
			System.exit(1);
		}
	}

}
