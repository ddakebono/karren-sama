/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class ListRole extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        StringBuilder roleString = new StringBuilder();
        for (IRole role : event.getGuild().getRoles()) {
            if (role.getName().contains("(A)")) {
                if(roleString.length()>0){
                    roleString.append(", ");
                }
                roleString.append("\"");
                roleString.append(role.getName().replace("(A)", "").trim());
                roleString.append("\"");
            }
        }

        if(roleString.length()>0)
            msg = interaction.replaceMsg(msg, "%roles", roleString.toString());
        else
            return interaction.getRandomTemplate("notsetup").getTemplate();

        return msg;
    }

    @Override
    public String getTagName() {
        return "ListRole";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.MANAGE_ROLES);
    }
}
