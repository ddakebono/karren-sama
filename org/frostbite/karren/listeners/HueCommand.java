package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Logging;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HueCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		PircBotX bot = event.getBot();
		String cmd = "Hue";
		String message = event.getMessage();
		if(message.toLowerCase().startsWith(cmd.toLowerCase())){
			bot.sendMessage(event.getChannel(), "Hue");
			GlobalVars.hueCount++;
			if(GlobalVars.hueCount >= 3){
				bot.kick(event.getChannel(), event.getUser(), "Hue hue hue!");
				GlobalVars.hueCount = 0;
				bot.sendMessage(event.getChannel(), "Wow, " + event.getUser().getNick() + " just got #rekt.");
				Logging.log("Karren-sama has kicked " + event.getUser().getNick() + "For Overhue");
			}
		}
	}

}
