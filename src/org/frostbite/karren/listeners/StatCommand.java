/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import java.util.Objects;
import java.util.function.Consumer;

public class StatCommand implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent event) {
        User bot = Karren.bot.client.getSelf().block();
        if(event.getMember().isPresent()) {
            assert bot != null;
            if (bot.getId().equals(event.getMember().get().getId()))
                return;
        }
        if(event.getMessage().getContent().isPresent()) {
            Guild guild = event.getGuild().block();
            if (event.getMessage().getContent().get().startsWith(Karren.bot.getGuildManager().getCommandPrefix(guild) + "stats")) {
                StringBuilder msg = new StringBuilder("**Karren-sama Stats**");
                msg.append("```Bot Uptime: ").append(KarrenUtil.calcAway(Karren.startTime));
                msg.append("\n-------------Build Info-------------");
                msg.append("\nBot Version: ").append(Karren.botVersion);
                msg.append("\nCommit ID: ").append(Karren.jarProps.getProperty("git.commit.id"));
                msg.append("\nCommit Time: ").append(Karren.jarProps.getProperty("git.commit.time"));
                msg.append("\nCommit Message: ").append(Karren.jarProps.getProperty("git.commit.message.full"));
                msg.append("\nBuild Branch: ").append(Karren.jarProps.getProperty("git.branch"));
                msg.append("\n-------------Bot Status-------------");
                msg.append("\nConnected Guilds: ").append(Objects.requireNonNull(Karren.bot.getClient().getGuilds().collectList().block()).size());
                msg.append("\nConnected Shards: Borked");
                msg.append("\nConnected Voice Channels: Borked");
                msg.append("\nInteraction System Tags: Borked");
                msg.append("\nTotal Users Visable: Borked");
                msg.append("\n-------------Cache Status-------------");
                msg.append("\nCached Reminders: ").append(Karren.bot.getSql().getDbReminderCache().size());
                msg.append("\nCached Users: ").append(Karren.bot.getSql().getDbUserCache().size());
                msg.append("\nCached Guild Users: ").append(Karren.bot.getSql().getDbGuildUserCache().size());
                msg.append("\nCached Guilds: ").append(Karren.bot.getSql().getDbGuildCache().size());
                msg.append("\nCached Word Counts: ").append(Karren.bot.getSql().getDbWordcountCache().size());
                msg.append("```");
                Objects.requireNonNull(event.getMessage().getChannel().block()).createMessage(msg.toString()).block();
            }
        }
    }
}
