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
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

import java.util.HashMap;

public class Return implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        HashMap<IUser, Boolean> departedUsers = ((Depart)Karren.bot.getInteractionManager().getHandlers().get("depart")).departedUsers;
        if(departedUsers.getOrDefault(event.getMessage().getAuthor(), true) || !interaction.isSpecialInteraction()) {
            UserRecord user = Karren.bot.getSql().getUserData(event.getMessage().getAuthor());
            if(departedUsers.putIfAbsent(event.getMessage().getAuthor(), false) != null)
                departedUsers.put(event.getMessage().getAuthor(), false);
            if (user.getTimeleft()!=null) {
                msg = msg.replace("%away", KarrenUtil.calcAway(user.getTimeleft().getTime()));
                user.setTimeleft(null);
                user.update();
                return msg;
            } else {
                if (interaction.isSpecialInteraction())
                    return null;
                else
                    return interaction.getRandomTemplatesFail();
            }
        }
        return null;
    }
}
