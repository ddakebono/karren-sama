/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.Arrays;

public class Parameter extends Tag {

    //Currently the parameter tag only works with Prefixed interactions, use on non prefixed interactions isn't possible yet.

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        String message = null;
        if(result.getEvent().getMessage().getContent().isPresent())
            message = result.getEvent().getMessage().getContent().get();
        if(interaction.getTriggers()!=null && message!=null) {
            Guild guild = result.getEvent().getGuild().block();
            for (String trigger : interaction.getTriggers()) {
                if (message.startsWith(Karren.bot.getGuildManager().getCommandPrefix(guild) + trigger)) {
                    message = message.replace(Karren.bot.getGuildManager().getCommandPrefix(guild) + trigger, "").trim();
                    break;
                }
                //Patch for prefixed interactions
                if (message.toLowerCase().startsWith(trigger) && !Arrays.asList(interaction.getTags()).contains("prefixed")){
                    message = message.toLowerCase().replace(trigger, "").trim();
                    break;
                }
            }
        }

        if(message!=null)
            interaction.setParameter(message);
        return msg;
    }

    @Override
    public String getTagName() {
        return "parameter";
    }

}
