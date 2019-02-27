/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ICategory;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class CreateTempChannel extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
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
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.MANAGE_CHANNELS);
    }
}
