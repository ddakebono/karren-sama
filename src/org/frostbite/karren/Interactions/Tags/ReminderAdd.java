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

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.frostbite.karren.Database.Objects.DbReminder;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderAdd extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        Optional<User> authorOpt = result.getEvent().getMessage().getAuthor();

        if(result.getEvent().getMessage().getContent().isPresent() && guild != null && authorOpt.isPresent()) {
            User author = authorOpt.get();
            String[] tempArray = result.getEvent().getMessage().getContent().get().split("that");
            Pattern timeMatch = Pattern.compile("(?:\\d*\\S)?\\d+ \\S+");
            if (interaction.getMentionedUsers().size()> 0 && tempArray.length == 2) {
                DbReminder reminder = new DbReminder();
                reminder.setReminderSent(false);
                reminder.setAuthorID(author.getId().asLong());
                reminder.setTargetID(Karren.bot.getSql().getGuildUser(guild, interaction.getMentionedUsers().get(0)).getGuildUserID());
                reminder.setMessage(tempArray[1].trim());
                reminder.setReminderTime(getRemindTime(timeMatch.matcher(tempArray[0].trim())));
                reminder.setChannelID(result.getEvent().getMessage().getChannelId().asLong());
                Karren.bot.getSql().addReminder(reminder);
                msg = interaction.replaceMsg(msg, "%target", interaction.getMentionedUsers().get(0).getUsername());
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
