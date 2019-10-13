/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;

public class KillCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //Catch all messages and log
        if(!event.getAuthor().isBot()) {
            Karren.log.debug("Message from: " + event.getAuthor().getName() + " | Channel: " + event.getChannel().getId() + " | Content: " + event.getMessage().getContentRaw());
            if (event.getMessage().getContentRaw().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "kill")) {
                if (event.getAuthor().getId().equals(Karren.conf.getOperatorDiscordID())) {
                    Karren.bot.killBot(event.getAuthor().getName());
                } else {
                    event.getChannel().sendMessage("Hold on a sec, this command can only be used by my operator.");
                }
            }
        }
    }
}
