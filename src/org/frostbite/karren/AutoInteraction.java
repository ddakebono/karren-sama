/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.frostbite.karren.Database.Objects.DbReminder;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class AutoInteraction extends Thread {

    private boolean kill = false;
    private boolean suspend = false;

    public void run(){
        Karren.bot.getSql().preloadReminderCache();
        while(!kill){
            if(!suspend) {
                List<DbReminder> remindersPastTime = Karren.bot.getSql().getDbReminderCache().stream().filter(x -> (x.getReminderTime().before(new Timestamp(System.currentTimeMillis())) && x.getReminderTime().getTime() != 0) && !x.reminderSent).collect(Collectors.toList());
                if (remindersPastTime.size() > 0) {
                    for (DbReminder reminder : remindersPastTime) {
                        reminder.setReminderSent(true);
                        reminder.update();
                        User author = Karren.bot.client.getUserById(reminder.authorID);
                        User target = Karren.bot.client.getUserById(reminder.targetID);
                        if(author!=null&&target!=null) {
                            String message = "Hey " + target.getAsMention() + ", " + author.getName() + " wanted me to remind you **\"" + reminder.getMessage() + "\"**";
                            TextChannel channel = Karren.bot.client.getTextChannelById(reminder.channelID);
                            if(channel!=null)
                                channel.sendMessage(message);
                        }
                        /*IUser author = Karren.bot.getClient().getUserByID(reminder.getAuthorID());
                        IUser target = Karren.bot.getClient().getUserByID(Karren.bot.getSql().getGuildUser(reminder.getTargetID()).getUserID());
                        MessageBuilder msg = new MessageBuilder(Karren.bot.getClient());
                        msg.withChannel(reminder.getChannelID());
                        msg.withContent("Hey <@" + target.getStringID() + ">, " + author.getName() + " wanted me to remind you **\"" + reminder.getMessage() + "\"**");
                        try {
                            msg.send();
                        } catch (DiscordException e) {
                            Karren.log.error(e.getErrorMessage());
                        }*/
                    }
                }

                /*for(IGuild guild : Karren.bot.getClient().getGuilds()){
                    if(Karren.bot.getGuildManager().getInteractionProcessor(guild))
                }*/

            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public void setKill(boolean kill){
        this.kill = kill;
    }
}
