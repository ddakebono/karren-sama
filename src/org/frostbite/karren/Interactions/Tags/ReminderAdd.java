/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Database.Objects.DbReminder;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderAdd extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {

        if(result.getEvent().isFromGuild()) {
            String[] tempArray = result.getEvent().getMessage().getContentDisplay().toLowerCase().split("that");
            Pattern timeMatch = Pattern.compile("(?:\\d*\\S)?\\d+ \\S+");
            if (interaction.getMentionedUsers().size()> 0 && tempArray.length == 2) {
                DbReminder reminder = new DbReminder();
                reminder.setReminderSent(false);
                reminder.setAuthorID(result.getEvent().getAuthor().getIdLong());
                reminder.setTargetID(interaction.getMentionedUsers().get(0).getIdLong());
                reminder.setMessage(tempArray[1].trim());
                reminder.setReminderTime(getRemindTime(timeMatch.matcher(tempArray[0].trim())));
                reminder.setChannelID(result.getEvent().getChannel().getIdLong());
                Karren.bot.getSql().addReminder(reminder);
                msg = interaction.replaceMsg(msg, "%target", interaction.getMentionedUsers().get(0).getName());
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        }
        return msg;
    }

    public Timestamp getRemindTime(Matcher matcher){
        long additionalTime = 0;
        while(matcher.find()){
            String[] timeExtract = matcher.group(0).split(" ");
            switch(timeExtract[1].toLowerCase()){
                case "week":
                case "weeks":
                    timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*7);
                case "day":
                case "days":
                    timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*24);
                case "hour":
                case "hours":
                    timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*60);
                case "minute":
                case "minutes":
                    timeExtract[0] = String.valueOf(Integer.parseInt(timeExtract[0])*60);
                case "second":
                case "seconds":
                    additionalTime+=Integer.parseInt(timeExtract[0])*1000;
                    break;
            }
        }
        if(additionalTime>0)
            return new Timestamp(System.currentTimeMillis()+additionalTime);
        return new Timestamp(0);
    }

    @Override
    public String getTagName() {
        return "reminderadd";
    }

}
