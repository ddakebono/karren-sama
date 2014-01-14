/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class UserLogon extends ListenerAdapter<PircBotX>{
	public void onJoin(JoinEvent<PircBotX> event){
		if(!event.getUser().getNick().equalsIgnoreCase(GlobalVars.botname)){
			for(User user : event.getChannel().getUsers()){
				GlobalVars.userList.clear();
				GlobalVars.userList.add(user.getNick());
			}
		}
	}
}
