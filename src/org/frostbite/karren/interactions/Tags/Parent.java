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
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.util.stream.Collectors;

public class Parent implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        for(String child : interaction.getChildInteractions()){
            for(Interaction childInt : Karren.bot.getInteractionManager().getInteractions().stream().filter(Interaction::isChild).filter((p)-> p.getIdentifier().equalsIgnoreCase(child)).collect(Collectors.toList())){
                childInt.setEnabled(true);
            }
        }
        return msg;
    }
}
