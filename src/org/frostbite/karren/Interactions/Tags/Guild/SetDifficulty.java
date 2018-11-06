/*
 * Copyright (c) 2017 Owen Bennett.
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

public class SetDifficulty extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        if(interaction.hasParameter()){
            int difficulty = -1;
            try {
                difficulty = Integer.parseInt(interaction.getParameter());
            } catch (NumberFormatException e){
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
            if(difficulty>=0 && difficulty<=100){
                DbGuild dbGuild = Karren.bot.getSql().getGuild(guild);
                dbGuild.setRollDifficulty(difficulty);
                dbGuild.update();
                msg = interaction.replaceMsg(msg,"%newdiff", String.valueOf(difficulty));
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setdifficulty";
    }

}
