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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class ListRole extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        StringBuilder roleString = new StringBuilder();
        for (Role role : result.getEvent().getGuild().getRoles()) {
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
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_ROLES, Permission.MESSAGE_WRITE};
    }
}
