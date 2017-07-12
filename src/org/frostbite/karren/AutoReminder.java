/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import org.frostbite.karren.Database.Objects.DbReminder;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class AutoReminder extends Thread {

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
                        IUser author = Karren.bot.getClient().getUserByID(reminder.getAuthorID());
                        IUser target = Karren.bot.getClient().getUserByID(Karren.bot.getSql().getGuildUser(reminder.getTargetID()).getUserID());
                        MessageBuilder msg = new MessageBuilder(Karren.bot.getClient());
                        msg.withChannel(reminder.getChannelID());
                        msg.withContent("Hey <@" + target.getStringID() + ">, " + author.getName() + " wanted me to remind you **\"" + reminder.getMessage() + "\"**");
                        try {
                            msg.send();
                        } catch (DiscordException e) {
                            Karren.log.error(e.getErrorMessage());
                        }
                    }
                }
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
