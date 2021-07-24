/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.entities.User;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.List;

public class RollFailStreaks extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        List<DbGuildUser> users = Karren.bot.getSql().getTopRollFailStreaks();

        for(DbGuildUser user : users){
            User discordUser = Karren.bot.getClient().getUserById(user.getUserID());
            if(discordUser==null)
                discordUser = Karren.bot.getClient().retrieveUserById(user.getUserID()).complete();
            interaction.addEmbedField(new InteractionEmbedFields(discordUser != null ? discordUser.getName() : "Missing Name", "With a streak of " + user.getHighestRollFail() + " fails! (" + user.getTotalRolls() + " rolls / " + user.getWinningRolls() + " wins)", false));
        }

        return msg;
    }

    @Override
    public String getTagName() {
        return "RollFailStreaks";
    }
}
