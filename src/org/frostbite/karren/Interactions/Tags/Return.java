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

import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

public class Return extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().isFromGuild()) {
            if (Karren.bot.departedUsers.getOrDefault(result.getEvent().getAuthor().getIdLong(), true) || !interaction.isSpecialInteraction()) {
                DbUser user = Karren.bot.getSql().getUserData(result.getEvent().getAuthor());
                if (Karren.bot.departedUsers.putIfAbsent(result.getEvent().getAuthor().getIdLong(), false) != null)
                    Karren.bot.departedUsers.put(result.getEvent().getAuthor().getIdLong(), false);
                if (user.getTimeLeft() != null) {
                    msg = interaction.replaceMsg(msg, "%away", KarrenUtil.calcAway(user.getTimeLeft().getTime()));
                    user.setTimeLeft(null);
                    user.update();
                    return msg;
                } else {
                    if (interaction.isSpecialInteraction()) {
                        interaction.stopProcessing();
                        return null;
                    }
                    else
                        return interaction.getRandomTemplate("fail").getTemplate();
                }
            } else {
                if (interaction.getMentionedUsers().size()>0) {
                    boolean isDeparted = Karren.bot.departedUsers.getOrDefault(result.getEvent().getMessage().getMentionedUsers().get(0), false);
                    if (interaction.isSpecialInteraction() && isDeparted) {
                        DbUser mention = Karren.bot.getSql().getUserData(interaction.getMentionedUsers().get(0));
                        msg = interaction.getRandomTemplate("fail").getTemplate();
                        msg = interaction.replaceMsg(msg, "%name", result.getEvent().getAuthor().getAsMention());
                        msg = interaction.replaceMsg(msg, "%mention", interaction.getMentionedUsers().get(0).getName());
                        msg = interaction.replaceMsg(msg, "%away", KarrenUtil.calcAway(mention.getTimeLeft().getTime()));
                        return msg;
                    }
                }
            }
        }
        interaction.stopProcessing();
        return null;
    }

    @Override
    public String getTagName() {
        return "return";
    }
}
