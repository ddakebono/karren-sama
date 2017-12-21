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
import org.frostbite.karren.interactions.InteractionProcessor;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class InteractionCommands extends ListenerAdapter {
    public void onMessage(MessageEvent event){
        if(Karren.conf.getEnableInteractions()){
            String message = null;
            InteractionProcessor ip = Karren.bot.getGuildManager().getInteractionProcessor(event.getChannel());
            if(ip!=null) {
                if(!Karren.bot.getSql().getGuildUser(event.getChannel(), event.getUser()).isIgnoreCommands()) {
                    message = ip.handle(event);
                    if (message != null) {
                        event.getChannel().send().message(message);
                    } else if (event.getMessage().toLowerCase().contains(Karren.bot.client.getNick().toLowerCase())) {
                        event.getChannel().send().message("It's not like I wanted to answer anyways....baka. (Use \"" + Karren.conf.getCommandPrefix() + "help\" to view all usable interactions)");
                    }
                }
            } else {
                event.getChannel().send().message("Please stand by, the interaction processor is still initializing...");
            }
        }
    }




}
