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
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class InteractionCommands implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        if(Karren.conf.getEnableInteractions().equalsIgnoreCase("true")){
            MessageBuilder response = null;
            try {
                response = Karren.bot.getInteractionManager().handle(event);
            } catch (HTTP429Exception | DiscordException e) {
                e.printStackTrace();
            }
            if(response!=null) {
                try {
                    response.send();
                } catch (HTTP429Exception | DiscordException | MissingPermissionsException e) {
                    e.printStackTrace();
                }
            } else if(event.getMessage().getContent().toLowerCase().contains(Karren.bot.getClient().getOurUser().getName().toLowerCase())) {
                try {
                    event.getMessage().getChannel().sendMessage("It's not like I wanted to answer anyways....baka. (Use \"" + Karren.conf.getCommandPrefix() + "help\" to view all usable interactions)");
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
