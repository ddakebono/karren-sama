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
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class StatCommand implements IListener<MessageReceivedEvent> {
    @Override
    public void handle(MessageReceivedEvent event) {
        if(event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "stats")){
            MessageBuilder msg = new MessageBuilder(event.getClient());
            msg.withChannel(event.getChannel());
            msg.withContent("**Karren-sama Stats**");
            msg.appendContent("```Bot Uptime: " + KarrenUtil.calcAway(Karren.startTime));
            msg.appendContent("\nBot Version: " + Karren.botVersion);
            msg.appendContent("\nCommit ID: " + Karren.jarProps.getProperty("git.commit.id"));
            msg.appendContent("\nCommit Time: " + Karren.jarProps.getProperty("git.commit.time"));
            msg.appendContent("\nCommit Message: " + Karren.jarProps.getProperty("git.commit.message.full"));
            msg.appendContent("\nBuild Branch: " + Karren.jarProps.getProperty("git.branch"));
            msg.appendContent("\nConnected Guilds: " + Karren.bot.getClient().getGuilds().size());
            msg.appendContent("\nConnected Shards: " + Karren.bot.getClient().getShardCount());
            msg.appendContent("\nConnected Voice Channels: " + Karren.bot.getClient().getConnectedVoiceChannels().size());
            msg.appendContent("```");
            msg.send();
        }
    }
}
