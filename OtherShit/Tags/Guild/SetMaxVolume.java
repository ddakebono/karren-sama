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

import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;


public class SetMaxVolume extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        if(interaction.hasParameter()) {
            int volume = Integer.parseInt(interaction.getParameter().trim());
            if (volume >= 0 && volume <= 100) {
                DbGuild dbGuild = Karren.bot.getSql().getGuild(guild);
                dbGuild.setMaxVolume(volume);
                dbGuild.update();
                msg = interaction.replaceMsg(msg, "%volume", Integer.toString(volume));
            } else {
                msg = interaction.getRandomTemplate("noparam").getTemplate();
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setmaxvolume";
    }

}
