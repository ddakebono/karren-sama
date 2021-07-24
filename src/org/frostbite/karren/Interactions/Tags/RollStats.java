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

import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import java.util.Date;

public class RollStats extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(result.getEvent().getGuild(), result.getEvent().getAuthor());
        if(dbGuildUser!=null){
            msg = interaction.replaceMsg(msg, "%totalrolls", Integer.toString(dbGuildUser.getTotalRolls()));
            msg = interaction.replaceMsg(msg, "%totalwins", Integer.toString(dbGuildUser.getWinningRolls()));
            msg = interaction.replaceMsg(msg, "%failstreak", Integer.toString(dbGuildUser.getHighestRollFail()));
            msg = interaction.replaceMsg(msg, "%winrate", String.format("%1$,.2f",((double)dbGuildUser.getWinningRolls()/dbGuildUser.getTotalRolls())*100));
            if(dbGuildUser.getRollTimeout()!=null && !dbGuildUser.getRollTimeout().before(new Date()))
                msg = interaction.replaceMsg(msg, "%timeleft", KarrenUtil.calcTimeDiff(dbGuildUser.getRollTimeout().getTime(), System.currentTimeMillis()));
            else
                msg = interaction.replaceMsg(msg, "%timeleft", "You can roll!");
        }

        return msg;
    }

    @Override
    public String getTagName() {
        return "RollStats";
    }
}
