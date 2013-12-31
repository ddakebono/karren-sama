package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PartEvent;

public class UserLogoff extends ListenerAdapter<PircBotX>{
	public void onPart(PartEvent<PircBotX> event){
		if(!event.getUser().getNick().equalsIgnoreCase(GlobalVars.botname)){
			for(User user : event.getChannel().getUsers()){
				GlobalVars.userList.clear();
				GlobalVars.userList.add(user.getNick());
			}
		}
	}
}
