/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class NewsCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event) throws Exception{
		String message = event.getMessage();
		String cmd = ((KarrenBot)event.getBot()).getBotConf().getCommandPrefix() + "news";
		User user = event.getUser();
		if(message.startsWith(cmd)){
			message = message.replaceFirst(cmd, "").trim();
			if(event.getChannel().isOp(event.getUser()) || event.getChannel().hasVoice(event.getUser())){
				if(message.length() > 0){
                    ((KarrenBot)event.getBot()).getSql().addNewsPost(message, user.getNick());
                    ((KarrenBot)event.getBot()).getLog().info("A news update was posted by " + user.getNick(), false);
				} else {
					event.respond("You seem to be missing the news post, please supply a post after the command. (ex. .news This is my post!)");
				}
			} else {
				event.respond("You do not have the required permission to do this. (Not Voice or OP)");
			}
			
		}
	}
}
