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

import net.dv8tion.jda.api.entities.User;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class SetFilter extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if (interaction.getMentionedUsers().size() > 0) {
            User user = interaction.getMentionedUsers().get(0);

            if(!user.getId().equals(Karren.conf.getOperatorDiscordID())) {
                if (!user.getId().equals(result.getEvent().getAuthor().getName())) {
                    DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(result.getEvent().getGuild(), user);
                    if (dbGuildUser.isIgnoreCommands()) {
                        dbGuildUser.setIgnoreCommands(false);
                        msg = interaction.replaceMsg(msg, "%setting", "disabled");
                    } else {
                        dbGuildUser.setIgnoreCommands(true);
                        msg = interaction.replaceMsg(msg, "%setting", "enabled");
                    }

                    msg = interaction.replaceMsg(msg, "%user", user.getName());
                    dbGuildUser.update();
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            } else {
                msg = interaction.getRandomTemplate("opError").getTemplate();
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setfilter";
    }

}
