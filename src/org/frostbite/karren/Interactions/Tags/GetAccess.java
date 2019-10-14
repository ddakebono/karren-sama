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
import org.frostbite.karren.Karren;

import java.util.Objects;
import java.util.Optional;

public class GetAccess extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().isFromGuild()) {
            if (result.getEvent().getMessage().getChannel().getIdLong()==Karren.bot.sql.getGuild(result.getEvent().getGuild()).getWelcomeChannel()) {
                String param = Karren.bot.sql.getGuild(result.getEvent().getGuild()).getAccessRole();
                Optional<Role> selectedRole = result.getEvent().getGuild().getRoles().stream().filter(x -> x.getName().toLowerCase().startsWith(param.toLowerCase())).findFirst();
                if (selectedRole.isPresent()) {
                    Role roleGet = selectedRole.get();
                    msg = interaction.replaceMsg(msg, "%role", "\"" + roleGet.getName().replace("(A)", "").trim() + "\"");
                    if(Objects.requireNonNull(result.getEvent().getMember()).getRoles().contains(roleGet)) {
                        result.getEvent().getGuild().removeRoleFromMember(result.getEvent().getMember(), roleGet).complete();
                    } else {
                        result.getEvent().getGuild().addRoleToMember(result.getEvent().getMember(), roleGet).complete();
                    }
                } else {
                    return interaction.getRandomTemplate("fail").getTemplate();
                }
                return msg;
            }
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "GetAccess";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_ROLES, Permission.MESSAGE_WRITE};
    }
}