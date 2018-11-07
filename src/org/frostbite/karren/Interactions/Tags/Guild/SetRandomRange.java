/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Guild;

import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class SetRandomRange extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()) {
            Guild guild = result.getEvent().getGuild().block();
            int range = Integer.parseInt(interaction.getParameter().trim());
            if (range >= 0 && range <= 5) {
                DbGuild dbGuild = Karren.bot.getSql().getGuild(guild);
                dbGuild.setRandomRange(range);
                dbGuild.update();
                msg = interaction.replaceMsg(msg, "%range", Integer.toString(range));
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
        return "randomrange";
    }

}
