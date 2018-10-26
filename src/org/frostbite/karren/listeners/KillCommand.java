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

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.frostbite.karren.Karren;

import java.util.function.Consumer;

public class KillCommand implements Consumer<MessageCreateEvent> {

    @Override
	public void accept(MessageCreateEvent event){
		String message = event.getMessage().toString();
        String cmd = Karren.bot.getGuildManager().getCommandPrefix(event.getGuild().block()) + "kill";
        if(message.toLowerCase().startsWith(cmd)) {
            
            if (event.getAuthor().getStringID().equals(Karren.conf.getOperatorDiscordID())) {
                Karren.bot.killBot(event.getMessage().getAuthor().getName());
            } else {
                event.getChannel().sendMessage("Hold on a sec, this command can only be used by my operator.");
            }
        }
	}
}
