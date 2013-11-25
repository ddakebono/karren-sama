package org.frostbite.karren.listeners;

import java.util.Random;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class RandCommand extends ListenerAdapter{
	public void onMessage(MessageEvent event) throws Exception{
		String cmd = "Random, ";
		String message = event.getMessage();
		PircBotX bot = event.getBot();
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			String[] choiceSet = message.split(",");
			for(int i=1; i==choiceSet.length; i++){
				choiceSet[i] = choiceSet[i].trim().replaceFirst(" ", "");
			}
			String choice = getRandom(choiceSet);
			event.respond(choice);
		}
	}
	public static String getRandom (String[] array){
		int randGen = new Random().nextInt(array.length);
		String chosen = array[randGen];
		return chosen;
	}
}
