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

import net.dv8tion.jda.api.entities.Guild;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.math.BigDecimal;
import java.util.List;

public class RollTopGuilds extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        List<Object[]> guildRanks = Karren.bot.getSql().getGuildRollsTop();

        for (Object[] guildRank : guildRanks) {
            Guild guild = Karren.bot.getClient().getGuildById((String) guildRank[0]);
            String winrate = String.format("%1$,.2f",(((BigDecimal)guildRank[2]).doubleValue()/((BigDecimal) guildRank[1]).doubleValue())*100);
            interaction.addEmbedField(new InteractionEmbedFields(guild!=null?guild.getName():"Missing Guild", "With " + guildRank[1] + " total rolls and " + guildRank[2] + " wins! (" + winrate + "% winrate)", false));
        }

        return msg;
    }

    @Override
    public String getTagName() {
        return "rolltopguilds";
    }
}
