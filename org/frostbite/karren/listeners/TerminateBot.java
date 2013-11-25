package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.QuitEvent;

public class TerminateBot extends ListenerAdapter{
	public void onQuit(QuitEvent event) throws Exception{
		PircBotX bot = event.getBot();
		String botName = GlobalVars.botname;
		if(event.getUser().getNick() == GlobalVars.botname){
			System.exit(1);
		}
	}

}
