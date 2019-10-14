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

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

public class AddRole extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()) {
            LinkedList<Role> assignableRoles = new LinkedList<>();
            for (Role role : result.getEvent().getGuild().getRoles())
                if (role.getName().contains("(A)"))
                    assignableRoles.add(role);
            if(assignableRoles.size()==0)
                return interaction.getRandomTemplate("notsetup").getTemplate();
            String param = interaction.getParameter();
            Optional<Role> selectedRole = assignableRoles.stream().filter(x -> x.getName().toLowerCase().startsWith(param.toLowerCase())).findFirst();
            if(selectedRole.isPresent()){
                Role roleGet = selectedRole.get();
                msg = interaction.replaceMsg(msg, "%role", "\"" + roleGet.getName().replace("(A)", "").trim() + "\"");
                if(Objects.requireNonNull(result.getEvent().getMember()).getRoles().contains(roleGet)) {
                    msg = interaction.replaceMsg(msg, "%setting", "Removed");
                    result.getEvent().getGuild().removeRoleFromMember(result.getEvent().getMember(), roleGet).complete();
                } else {
                    msg = interaction.replaceMsg(msg, "%setting", "Added");
                    result.getEvent().getGuild().addRoleToMember(result.getEvent().getMember(), roleGet).complete();
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
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_ROLES, Permission.MESSAGE_WRITE};
    }
}
