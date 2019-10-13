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

import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class SetPrefix extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(interaction.hasParameter()){
            String param = interaction.getParameter();
            DbGuild dbGuild = Karren.bot.getSql().getGuild(result.getEvent().getGuild());
            dbGuild.setCommandPrefix(param.trim());
            dbGuild.update();
            msg = interaction.replaceMsg(msg,"%prefix", param);
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setprefix";
    }

}
