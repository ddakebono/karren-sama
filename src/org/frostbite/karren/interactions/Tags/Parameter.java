/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class Parameter implements Tag {

    //Currently the parameter tag only works with Prefixed interactions, use on non prefixed interactions isn't possible yet.

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        for(String trigger : interaction.getTriggers()) {
            if(event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix() + trigger)) {
                message = message.replace(Karren.conf.getCommandPrefix() + trigger, "").trim();
                break;
            }
        }
        interaction.setParameter(message);
        return msg;
    }
}
