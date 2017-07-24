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
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class KillCommand implements IListener<MessageReceivedEvent> {
	public void handle(MessageReceivedEvent event){
		String message = event.getMessage().getContent();
        String cmd = Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "kill";
        if(message.toLowerCase().startsWith(cmd)) {
            if (event.getAuthor().getStringID().equals(Karren.conf.getOperatorDiscordID())) {
                Karren.bot.killBot(event.getMessage().getAuthor().getName());
            } else {
                event.getChannel().sendMessage("Hold on a sec, this command can only be used by my operator.");
            }
        }
	}
}
