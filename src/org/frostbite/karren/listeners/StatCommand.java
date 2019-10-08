/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import javax.annotation.Nonnull;

public class StatCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "stats")) {
            StringBuilder msg = new StringBuilder("**Karren-sama Stats**");
            msg.append("```Bot Uptime: ").append(KarrenUtil.calcAway(Karren.startTime));
            msg.append("\n-------------Build Info-------------");
            msg.append("\nBot Version: ").append(Karren.botVersion);
            msg.append("\nCommit ID: ").append(Karren.jarProps.getProperty("git.commit.id"));
            msg.append("\nCommit Time: ").append(Karren.jarProps.getProperty("git.commit.time"));
            msg.append("\nCommit Message: ").append(Karren.jarProps.getProperty("git.commit.message.full"));
            msg.append("\nBuild Branch: ").append(Karren.jarProps.getProperty("git.branch"));
            msg.append("\n-------------Bot Status-------------");
            msg.append("\nConnected Guilds: ").append(Karren.bot.getClient().getGuilds().size());
            msg.append("\nShardID: ").append(Karren.bot.getClient().getShardInfo().getShardId());
            msg.append("\nConnected Voice Channels: ").append(Karren.bot.getClient().getAudioManagers().stream().filter(AudioManager::isConnected).count());
            msg.append("\nInteraction System Tags: Borked");
            msg.append("\nTotal Users Visable: ").append(Karren.bot.getClient().getUsers().size());
            msg.append("\n-------------Cache Status-------------");
            msg.append("\nCached Reminders: ").append(Karren.bot.getSql().getDbReminderCache().size());
            msg.append("\nCached Users: ").append(Karren.bot.getSql().getDbUserCache().size());
            msg.append("\nCached Guild Users: ").append(Karren.bot.getSql().getDbGuildUserCache().size());
            msg.append("\nCached Guilds: ").append(Karren.bot.getSql().getDbGuildCache().size());
            msg.append("\nCached Word Counts: ").append(Karren.bot.getSql().getDbWordcountCache().size());
            msg.append("```");
            MessageAction msgSend = event.getChannel().sendMessage(msg.toString());
            msgSend.queue();
        }
    }
}
