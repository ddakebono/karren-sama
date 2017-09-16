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

import org.frostbite.karren.Database.Objects.DbReminder;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderCheck extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        List<DbReminder> reminders = Arrays.stream(Karren.bot.getSql().getReminder(Karren.bot.getSql().getGuildUser(event.getGuild(), event.getAuthor()))).filter(x -> x.getReminderTime().before(new Timestamp(System.currentTimeMillis()))).collect(Collectors.toList());
        if(reminders.size()>0){
            DbReminder alert = reminders.get(0);
            alert.setReminderSent(true);
            alert.update();
            msg = interaction.replaceMsg(msg,"%author", event.getClient().getUserByID(alert.authorID).getName());
            msg = interaction.replaceMsg(msg,"%message", alert.getMessage());
            Karren.bot.getSql().getDbReminderCache().remove(alert);
            return msg;
        } else {
            return null;
        }
    }

    @Override
    public String getTagName() {
        return "remindercheck";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
