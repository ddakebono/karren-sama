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

import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class HelpCommand implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        if(event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix() + "help")){
            MessageBuilder helpMsg = new MessageBuilder(bot);
            try {
                helpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                helpMsg.withContent("```\n");
                for (Interaction help : Karren.bot.getInteractionManager().getInteractions()) {
                    if(KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getClient(), help.getPermissionLevel()))
                        helpMsg.appendContent( help.getIdentifier() + " : " + help.getHelptext() + "\n");
                }
                helpMsg.appendContent("```").send();
            } catch (DiscordException | HTTP429Exception | MissingPermissionsException e) {
                e.printStackTrace();
            }
        }
    }
}
