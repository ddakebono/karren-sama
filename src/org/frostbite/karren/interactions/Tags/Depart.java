/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Database.Models.tables.records.UserRecord;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class Depart implements Tag {

    HashMap<IUser, Boolean> departedUsers = new HashMap<>();

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        UserRecord user = Karren.bot.getSql().getUserData(event.getMessage().getAuthor());
        if(user.getTimeleft()!=null){
            msg = interaction.getRandomTemplatesFail();
        } else {
            departedUsers.put(event.getMessage().getAuthor(), true);
        }
        user.setTimeleft(new Timestamp(new Date().getTime()));
        user.update();
        return msg;
    }
}
