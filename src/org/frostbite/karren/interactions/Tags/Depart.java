/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class Depart extends Tag {

    HashMap<User, Boolean> departedUsers = new HashMap<>();

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageEvent event) {
        DbUser user = Karren.bot.getSql().getUserData(event.getUser());
        if(user.getTimeLeft()!=null){
            msg = interaction.getRandomTemplate("fail").getTemplate();
        } else {
            departedUsers.put(event.getUser(), true);
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
