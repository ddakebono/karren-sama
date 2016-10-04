/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.d4j.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.FinishEvent;
import net.dv8tion.jda.player.hooks.events.StopEvent;
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class D4JPlayerVoiceExit extends PlayerListenerAdapter {
    @Override
    public void onFinish(FinishEvent event){
        leaveChannels();
    }
    @Override
    public void onStop(StopEvent event){
        leaveChannels();
    }

    private void leaveChannels(){
        for(IVoiceChannel channel : Karren.bot.getClient().getConnectedVoiceChannels()){
            MusicPlayer player = (MusicPlayer)channel.getGuild().getAudioManager().getAudioProvider();
            if(player.isStopped() || player.getAudioQueue().size()==0){
                player.getAudioQueue().clear();
                channel.leave();
            }
        }
    }
}
