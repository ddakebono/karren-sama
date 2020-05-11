/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;
import java.util.Objects;

public class VoiceLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        checkChannelState(event.getGuild(), event.getChannelLeft());
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        checkChannelState(event.getGuild(), event.getChannelLeft());
    }

    private void checkChannelState(Guild guild, VoiceChannel channel){
        GuildMusicManager gmm = Karren.bot.getGuildMusicManager(guild);
        if(gmm.scheduler.isPlaying() && Objects.equals(Objects.requireNonNull(Objects.requireNonNull(guild.getMember(Karren.bot.client.getSelfUser())).getVoiceState()).getChannel(), channel)){
            if(channel.getMembers().size()==1){
                gmm.scheduler.stopQueue();
            }
        }
    }
}
