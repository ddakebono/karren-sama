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
import discord4j.core.object.entity.User;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class SetFilter extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getMember().isPresent()) {
            User author = result.getEvent().getMember().get();
            Guild guild = result.getEvent().getGuild().block();
            if (interaction.getMentionedUsers().size() > 0) {
                User user = interaction.getMentionedUsers().get(0);

                if (!user.getId().equals(author.getId())) {
                    DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(guild, user);
                    if (dbGuildUser.isIgnoreCommands()) {
                        dbGuildUser.setIgnoreCommands(false);
                        msg = interaction.replaceMsg(msg, "%setting", "disabled");
                    } else {
                        dbGuildUser.setIgnoreCommands(true);
                        msg = interaction.replaceMsg(msg, "%setting", "enabled");
                    }

                    msg = interaction.replaceMsg(msg, "%user", user.getUsername());
                    dbGuildUser.update();
                } else {
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                }
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "setfilter";
    }

}
