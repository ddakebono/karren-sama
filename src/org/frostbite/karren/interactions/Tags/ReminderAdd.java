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
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderAdd extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String[] tempArray = event.getMessage().getContent().split("that");
        Pattern timeMatch = Pattern.compile("(?:\\d*\\S)?\\d+ \\S+");
        if(event.getMessage().getMentions().size()>0 && tempArray.length==2){
            DbReminder reminder = new DbReminder();
            reminder.setReminderSent(false);
            reminder.setAuthorID(event.getAuthor().getLongID());
            reminder.setTargetID(Karren.bot.getSql().getGuildUser(event.getGuild(), event.getMessage().getMentions().get(0)).getGuildUserID());
            reminder.setMessage(tempArray[1].trim());
            reminder.setReminderTime(getRemindTime(timeMatch.matcher(tempArray[0].trim())));
            Karren.bot.getSql().addReminder(reminder);
            msg = msg.replace("%target", event.getMessage().getMentions().get(0).getName());
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    public Timestamp getRemindTime(Matcher matcher){
        long additionalTime = 0;
        if(matcher.find()){
            for(int i=0; i<matcher.groupCount(); i++){
                String[] timeExtract = matcher.group(i).split(" ");
                switch(timeExtract[1].toLowerCase()){
                    case "weeks":
                        timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*7);
                    case "days":
                        timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*24);
                    case "hours":
                        timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*60);
                    case "minutes":
                        timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*60);
                    case "seconds":
                        additionalTime+=Integer.parseInt(timeExtract[0])*1000;
                        break;
                }
            }
        }
        return new Timestamp(System.currentTimeMillis()+additionalTime);
    }

    @Override
    public String getTagName() {
        return "reminderadd";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
