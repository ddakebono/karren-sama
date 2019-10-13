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
import org.frostbite.karren.Interactions.TagHelperClasses.DepartedUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import java.util.Optional;

public class Return extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().isFromGuild()) {
            Optional<DepartedUser> departedUser = Karren.bot.departedUsers.stream().filter(x -> x.userID == result.getEvent().getAuthor().getIdLong()).findFirst();
            if (!interaction.isSpecialInteraction()) {
                DbUser user = Karren.bot.getSql().getUserData(result.getEvent().getAuthor());
                if (!departedUser.isPresent())
                    Karren.bot.departedUsers.add(new DepartedUser(result.getEvent().getAuthor().getIdLong(), false));
                if (user.getTimeLeft() != null) {
                    msg = interaction.replaceMsg(msg, "%away", KarrenUtil.calcAway(user.getTimeLeft().getTime()));
                    user.setTimeLeft(null);
                    user.update();
                    return msg;
                } else {
                    if (interaction.isSpecialInteraction())
                        return null;
                    else
                        return interaction.getRandomTemplate("fail").getTemplate();
                }
            } else {
                if (interaction.getMentionedUsers().size()>0) {
                    long isDeparted = Karren.bot.departedUsers.stream().filter(x->x.userID==interaction.getMentionedUsers().get(0).getIdLong() && x.isDeparted).count();
                    if (interaction.isSpecialInteraction() && isDeparted>0) {
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
        return null;
    }

    @Override
    public String getTagName() {
        return "return";
    }
}
