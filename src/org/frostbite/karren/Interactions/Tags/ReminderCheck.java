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

import net.dv8tion.jda.api.entities.User;
import org.frostbite.karren.Database.Objects.DbReminder;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderCheck extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(Karren.conf.getAllowSQLRW()) {
            List<DbReminder> reminders = Arrays.stream(Karren.bot.getSql().getReminder(Karren.bot.getSql().getGuildUser(result.getEvent().getGuild(), result.getEvent().getAuthor()))).filter(x -> x.getReminderTime().before(new Timestamp(System.currentTimeMillis()))).collect(Collectors.toList());
            if (reminders.size() > 0) {
                DbReminder alert = reminders.get(0);
                alert.setReminderSent(true);
                alert.update();
                User remAuthor = Karren.bot.client.getUserById(alert.authorID);
                if(remAuthor!=null)
                    msg = interaction.replaceMsg(msg, "%author", remAuthor.getName());
                else
                    msg = interaction.replaceMsg(msg, "%author", "No Author");
                msg = interaction.replaceMsg(msg, "%message", alert.getMessage());
                Karren.bot.getSql().getDbReminderCache().remove(alert);
                return msg;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "remindercheck";
    }

}
