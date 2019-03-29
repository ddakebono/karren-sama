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
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.Optional;

public class GetAccess extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(event.getChannel().getName().equalsIgnoreCase("welcome")) {
            String param = Karren.bot.sql.getGuild(event.getGuild()).getAccessRole();
            Optional<IRole> selectedRole = event.getGuild().getRoles().stream().filter(x -> x.getName().toLowerCase().startsWith(param.toLowerCase())).findFirst();
            if (selectedRole.isPresent()) {
                IRole roleGet = selectedRole.get();
                msg = interaction.replaceMsg(msg, "%role", "\"" + roleGet.getName().replace("(A)", "").trim() + "\"");
                if (event.getAuthor().hasRole(roleGet)) {
                    event.getAuthor().removeRole(roleGet);
                } else {
                    event.getAuthor().addRole(roleGet);
                }
            } else {
                return interaction.getRandomTemplate("fail").getTemplate();
            }
            return msg;
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "GetAccess";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.MANAGE_ROLES);
    }
}