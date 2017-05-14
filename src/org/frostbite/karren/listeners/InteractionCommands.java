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
import org.frostbite.karren.interactions.InteractionProcessor;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.*;

public class InteractionCommands implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        if(Karren.conf.getEnableInteractions()){
            MessageBuilder response;
            InteractionProcessor ip = Karren.bot.getInteractionManager().getInteractionProcessor(event.getGuild());
            if(ip!=null) {
                response = ip.handle(event);
                if (response != null) {
                    try {
                        response.send();
                    } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                        e.printStackTrace();
                    }
                } else if (event.getMessage().getContent().toLowerCase().contains(Karren.bot.getClient().getOurUser().getName().toLowerCase())) {
                    try {
                        event.getMessage().getChannel().sendMessage("It's not like I wanted to answer anyways....baka. (Use \"" + Karren.conf.getCommandPrefix() + "help\" to view all usable interactions)");
                    } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                event.getChannel().sendMessage("Please stand by, the interaction processor is still initializing...");
            }
        }
    }




}
