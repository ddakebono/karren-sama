/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class KillCommand extends ListenerAdapter<PircBotX>{
	public void onMessage(MessageEvent<PircBotX> event){
		String message = event.getMessage();
		KarrenBot bot = (KarrenBot)event.getBot();
        String cmd = bot.getBotConf().getCommandPrefix() + "kill";
        String cmd2 = bot.getBotConf().getCommandPrefix() + "restart";
		if((event.getChannel().isOp(event.getUser())||event.getChannel().isOwner(event.getUser())) && (message.toLowerCase().startsWith(cmd)||message.toLowerCase().startsWith(cmd2))){
			bot.getLog().info("Bot has been killed by " + event.getUser().getNick());
            bot.botIsKill();
            if(message.toLowerCase().startsWith(cmd)){
                bot.stopBotReconnect();
            }
            bot.sendIRC().quitServer("Kill command fired, bot terminating.");
        } else {
			if(!event.getChannel().isOp(event.getUser()) && message.startsWith(cmd))
				event.respond("You can't tell me what to do! (Not Operator)");
		}
	}

}
