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

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;

public class VoiceLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        GuildMusicManager gmm = Karren.bot.getGuildMusicManager(event.getGuild());
        if(gmm.scheduler.isPlaying()){
            if(event.getChannelLeft().getMembers().size()==1){
                gmm.scheduler.stopQueue();
            }
        }
    }
}
