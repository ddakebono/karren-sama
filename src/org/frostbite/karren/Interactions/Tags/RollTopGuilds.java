/*
 * Copyright (c) 2019 Owen Bennett.
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
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.List;

public class RollTopGuilds extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        List<Object[]> guildRanks = Karren.bot.getSql().getGuildRollsTop();
        StringBuilder output = new StringBuilder();
        for(int i=0; i<guildRanks.size(); i++){
            Guild guild = Karren.bot.getClient().getGuildById((String) guildRanks.get(i)[0]);
            if(guild!=null)
                output.append(i+1).append(": ").append(guild.getName()).append(" with ").append(guildRanks.get(i)[1]).append(" total rolls\n");
            //output.append(i+1).append(": ").append(Karren.bot.getClient().getGuildByID(Long.valueOf((String) guildRanks.get(i)[0])).getName()).append(" with ").append(guildRanks.get(i)[1]).append(" total rolls\n");
        }
        msg = interaction.replaceMsg(msg,"%ranks", output.toString());
        return msg;
    }

    @Override
    public String getTagName() {
        return "rolltopguilds";
    }
}
