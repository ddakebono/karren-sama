/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import java.util.ArrayList;

import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class HashCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		String message = event.getMessage();
		String cmd = ".hash";
		int hashcode = 0;
		ArrayList<String> result = new ArrayList<String>();
		String[] data = new String[1];
		if(message.toLowerCase().startsWith(cmd) && event.getUser().isVerified()){
			event.getUser().send().message("Please wait, checking if you already have a hashcode.");
			data[0] = event.getUser().getNick();
			result = MySQLConnector.sqlPush("hash", "", data);
			hashcode = Integer.parseInt(result.get(0));
			event.getUser().send().message("Your hashcode is " + hashcode + ". Please keep this safe as it is the only way to update your now playing data.");
		}
	}

}
