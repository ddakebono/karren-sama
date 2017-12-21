/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class StatCommand extends ListenerAdapter {
    @Override
    public void onMessage(MessageEvent event) {
        if(event.getMessage().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getChannel()) + "stats")){
            String message = "**Karren-sama Stats**";
            message += "\n```Bot Uptime: " + KarrenUtil.calcAway(Karren.startTime);
            message += "\nBot Version: " + Karren.botVersion;
            message += "\nCommit ID: " + Karren.jarProps.getProperty("git.commit.id");
            message += "\nCommit Time: " + Karren.jarProps.getProperty("git.commit.time");
            message += "\nCommit Message: " + Karren.jarProps.getProperty("git.commit.message.full");
            message += "\nBuild Branch: " + Karren.jarProps.getProperty("git.branch");
            message += "\nConnected Guilds: " + Karren.bot.getClient().getUserBot().getChannels().size();
            message += "\nInteraction System Tags: " + Karren.bot.getGuildManager().getTagHandlers().size();
            message += "\nTotal Users in Channel: " + event.getChannel().getUsers().size();
            message += "\nCached Reminders: " + Karren.bot.getSql().getDbReminderCache().size();
            message += "\nCached Users: " + Karren.bot.getSql().getDbUserCache().size();
            message += "\nCached Guild Users: " + Karren.bot.getSql().getDbGuildUserCache().size();
            message += "\nCached Guilds: " + Karren.bot.getSql().getDbGuildCache().size();
            message += "\nCached Word Counts: " + Karren.bot.getSql().getDbWordcountCache().size();
            event.getChannel().send().message(message);
        }
    }
}
