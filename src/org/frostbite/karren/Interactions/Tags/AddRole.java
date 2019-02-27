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
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Optional;

public class AddRole extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()) {
            LinkedList<IRole> assignableRoles = new LinkedList<>();
            for (IRole role : event.getGuild().getRoles())
                if (role.getName().contains("(A)"))
                    assignableRoles.add(role);
            if(assignableRoles.size()==0)
                return interaction.getRandomTemplate("notsetup").getTemplate();
            String param = interaction.getParameter();
            Optional<IRole> selectedRole = assignableRoles.stream().filter(x -> x.getName().toLowerCase().startsWith(param.toLowerCase())).findFirst();
            if(selectedRole.isPresent()){
                IRole roleGet = selectedRole.get();
                msg = interaction.replaceMsg(msg, "%role", "\"" + roleGet.getName().replace("(A)", "").trim() + "\"");
                if(event.getAuthor().hasRole(roleGet)) {
                    msg = interaction.replaceMsg(msg, "%setting", "Removed");
                    event.getAuthor().removeRole(roleGet);
                } else {
                    msg = interaction.replaceMsg(msg, "%setting", "Added");
                    event.getAuthor().addRole(roleGet);
                }
            } else {
                return interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            return interaction.getRandomTemplate("noparam").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "AddRole";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.MANAGE_ROLES);
    }
}
