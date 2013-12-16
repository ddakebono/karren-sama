package org.frostbite.karren.listeners;

import java.util.Random;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class RandCommand{
	public static String randomList(String message){
		String[] choiceSet = message.split(",");
		for(int i=1; i==choiceSet.length; i++){
			choiceSet[i] = choiceSet[i].trim().replaceFirst(" ", "");
		}
		String choice = getRandom(choiceSet);
		return choice;
	}
	public static String getRandom (String[] array){
		int randGen = new Random().nextInt(array.length);
		String chosen = array[randGen];
		return chosen;
	}
}
