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
        if(!event.getAuthor().isBot()) {
            if (event.getMessage().getContentRaw().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "stats")) {
                String msg = "**Karren-sama Stats**" + "```Bot Uptime: " + KarrenUtil.calcAway(Karren.startTime) +
                        "\n-------------Build Info-------------" +
                        "\nBot Version: " + Karren.botVersion +
                        "\nCommit ID: " + Karren.jarProps.getProperty("git.commit.id") +
                        "\nCommit Time: " + Karren.jarProps.getProperty("git.commit.time") +
                        "\nCommit Message: " + Karren.jarProps.getProperty("git.commit.message.full") +
                        "\nBuild Branch: " + Karren.jarProps.getProperty("git.branch") +
                        "\n-------------Bot Status-------------" +
                        "\nConnected Guilds: " + Karren.bot.getClient().getGuilds().size() +
                        "\nShardID: " + Karren.bot.getClient().getShardInfo().getShardId() +
                        "\nConnected Voice Channels: " + Karren.bot.getClient().getAudioManagers().stream().filter(AudioManager::isConnected).count() +
                        "\nInteraction System Tags: Borked" +
                        "\nTotal Users Visable: " + Karren.bot.getClient().getUsers().size() +
                        "\n-------------Cache Status-------------" +
                        "\nCached Reminders: " + Karren.bot.getSql().getDbReminderCache().size() +
                        "\nCached Users: " + Karren.bot.getSql().getDbUserCache().size() +
                        "\nCached Guild Users: " + Karren.bot.getSql().getDbGuildUserCache().size() +
                        "\nCached Guilds: " + Karren.bot.getSql().getDbGuildCache().size() +
                        "\nCached Word Counts: " + Karren.bot.getSql().getDbWordcountCache().size() +
                        "```";
                MessageAction msgSend = event.getChannel().sendMessage(msg);
                msgSend.queue();
            }
        }
    }
}
