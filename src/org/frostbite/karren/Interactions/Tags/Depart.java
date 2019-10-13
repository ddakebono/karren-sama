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

import java.sql.Timestamp;
import java.util.Date;

public class Depart extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        DbUser user = Karren.bot.getSql().getUserData(result.getEvent().getAuthor());
        if(user.getTimeLeft()!=null){
            msg = interaction.getRandomTemplate("fail").getTemplate();
        } else {
            Karren.bot.departedUsers.add(new DepartedUser(result.getEvent().getAuthor().getIdLong(), true));
        }
        user.setTimeLeft(new Timestamp(new Date().getTime()));
        user.update();
        return msg;
    }

    @Override
    public String getTagName() {
        return "depart";
    }

}
