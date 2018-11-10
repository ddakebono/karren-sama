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
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.PermissionSet;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Karren;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;

import java.util.EnumSet;

public class CreateTempChannel extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        guild.getChannels().flatMap(x -> x.)
        ICategory category = event.getGuild().getCategories().stream().filter(x -> x.getName().toLowerCase().contains("temp voice channels")).findFirst().orElse(null);
        int maxChannels = 0;
        if(category!=null){
            String[] nameSplit = category.getName().split("-");
            if(nameSplit.length>1)
                maxChannels = Integer.parseInt(nameSplit[1].trim());
            if(interaction.hasParameter() && (category.getVoiceChannels().size()<maxChannels || maxChannels==0)) {
                IVoiceChannel channel = event.getGuild().createVoiceChannel(interaction.getParameter());
                msg = interaction.replaceMsg(msg, "%channel", channel.getName());
                channel.changeCategory(category);
                //Create invite to monitor when channel expires
                if(Karren.conf.isTestMode())
                    channel.createInvite(60, 1, false, true);
                else
                    channel.createInvite(86400, 1, false, true);
            } else if(interaction.hasParameter()) {
                return interaction.getRandomTemplate("limited").getTemplate();
            } else {
                return interaction.getRandomTemplate("noparam").getTemplate();
            }
        } else {
            return interaction.getRandomTemplate("nocategory").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "createtempchannel";
    }

    @Override
    public PermissionSet getRequiredPermissions() {
        return PermissionSet.of(Permission.SEND_MESSAGES, Permission.MANAGE_CHANNELS);
    }
}
