/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Guild;

import net.dv8tion.jda.api.entities.GuildChannel;
import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class GuildSettings extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        DbGuild dbGuild = Karren.bot.getSql().getGuild(result.getEvent().getGuild());
        interaction.replaceMsg(msg, "%difficulty", Integer.toString(dbGuild.getRollDifficulty()));
        interaction.replaceMsg(msg, "%maxvolume", Integer.toString(dbGuild.getMaxVolume()));
        interaction.replaceMsg(msg, "%prefix", ("\"" + Karren.bot.getGuildManager().getCommandPrefix(result.getEvent().getGuild()) + "\""));
        interaction.replaceMsg(msg, "%range", Integer.toString(dbGuild.getRandomRange()));
        if(dbGuild.getAccessRole()!=null){
            interaction.replaceMsg(msg, "%accessrole", dbGuild.getAccessRole());
        } else {
            interaction.replaceMsg(msg, "%accessrole", "Not Set");
        }
        if(dbGuild.getWelcomeChannel()!=0){
            GuildChannel welcome = result.getEvent().getGuild().getGuildChannelById(dbGuild.getWelcomeChannel());
            if(welcome!=null)
                interaction.replaceMsg(msg, "%welcomechan", welcome.getName());
            else
                interaction.replaceMsg(msg, "%welcomechan", "UNKNOWN CHANNEL");
        } else {
            interaction.replaceMsg(msg, "%welcomechan", "None Set");
        }
        if (dbGuild.getOverrideChannel() != 0) {
            GuildChannel overrideChannel = result.getEvent().getGuild().getGuildChannelById(dbGuild.getOverrideChannel());
            if (overrideChannel != null)
                interaction.replaceMsg(msg, "%override", overrideChannel.getName());
            else
                interaction.replaceMsg(msg, "%override", "UNKNOWN CHANNEL");
        } else {
            interaction.replaceMsg(msg, "%override", "None Set");
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "guildsettings";
    }

}
