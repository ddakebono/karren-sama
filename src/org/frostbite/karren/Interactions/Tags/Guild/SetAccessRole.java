/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Guild;

import net.dv8tion.jda.api.entities.Role;
import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.List;

public class SetAccessRole extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if (interaction.hasParameter()) {
            List<Role> roles = result.getEvent().getGuild().getRolesByName(interaction.getParameter(), true);
            if (roles.size() == 1) {
                DbGuild guild = Karren.bot.getSql().getGuild(result.getEvent().getGuild());
                guild.setAccessRole(roles.get(0).getName());
                guild.update();
                msg = interaction.replaceMsg(msg, "%role", roles.get(0).getName());
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            msg = interaction.getRandomTemplate("noparam").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setaccessrole";
    }
}

