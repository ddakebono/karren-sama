/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.Guild;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class DisableInteraction extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String parameter = interaction.getParameter();
        if(parameter!=null){
            msg = interaction.replaceMsg(msg,"%interaction", parameter);
            for(Interaction disable : Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild()).getInteractions().stream().filter((p)-> p.getIdentifier().equalsIgnoreCase(parameter)).collect(Collectors.toList())){
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

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
