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
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class RemoveStreamer extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.getMentionedUsers().size()==1){
            if(Karren.bot.sql.removeStreamer(interaction.getMentionedUsers().get(0), event.getGuild())){
                msg = interaction.replaceMsg(msg, "%user", interaction.getMentionedUsers().get(0).getName());
                return msg;
            } else {
                return interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            return interaction.getRandomTemplate("nouser").getTemplate();
        }
    }

    @Override
    public String getTagName() {
        return "removestreamer";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
