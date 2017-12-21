/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class KillCommand extends ListenerAdapter {
	public void onMessage(MessageEvent event){
		String message = event.getMessage();
        String cmd = Karren.bot.getGuildManager().getCommandPrefix(event.getChannel()) + "kill";
        if(message.toLowerCase().startsWith(cmd)) {
            if (event.getUser().getNick().equals(Karren.conf.getOperatorDiscordID())) {
                Karren.bot.killBot(event.getUser().getNick());
            } else {
                event.getChannel().send().message("Hold on a sec, this command can only be used by my operator.");
            }
        }
	}
}
