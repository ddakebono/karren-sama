/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class KillCommand implements IListener<MessageReceivedEvent> {
	public void handle(MessageReceivedEvent event){
		String message = event.getMessage().getContent();
		IDiscordClient bot = event.getClient();
        String cmd = Karren.conf.getCommandPrefix() + "kill";
        if(message.toLowerCase().startsWith(cmd)) {
            if (KarrenUtil.hasRole(event.getMessage().getAuthor(), bot, "Admins")) {
                Karren.bot.killBot(event.getMessage().getAuthor().getName());
            } else {
                if (!KarrenUtil.hasRole(event.getMessage().getAuthor(), bot, "Admins") && message.startsWith(cmd))
                    try {
                        event.getMessage().getChannel().sendMessage("You can't tell me what to do! (Not Admin)");
                    } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                        e.printStackTrace();
                    }
            }
        }
	}
}
