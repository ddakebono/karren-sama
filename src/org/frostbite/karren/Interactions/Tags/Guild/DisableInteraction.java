/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Guild;

import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.stream.Collectors;

public class DisableInteraction extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        String parameter = interaction.getParameter();
        if(parameter!=null){
            msg = interaction.replaceMsg(msg,"%interaction", parameter);
            for(Interaction disable : Karren.bot.getGuildManager().getInteractionProcessor(guild).getInteractions().stream().filter((p)-> p.getIdentifier().equalsIgnoreCase(parameter)).collect(Collectors.toList())){
                disable.setEnabled(false);
            }
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "disableinteraction";
    }

}
