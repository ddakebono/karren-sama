/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.frostbite.karren.interactions.Interaction;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MissingPermissionsException;

public class AudioResultHandler implements AudioLoadResultHandler {
    MessageReceivedEvent event;
    Interaction interaction;
    GuildMusicManager gm;
    String msg;

    boolean failed = false;

    public AudioResultHandler(MessageReceivedEvent event, Interaction interaction, GuildMusicManager gm, String msg) {
        this.event = event;
        this.interaction = interaction;
        this.gm = gm;
        this.msg = msg;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        connectToVoiceChannel(event);
        gm.scheduler.queue(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        connectToVoiceChannel(event);
        for (AudioTrack track : audioPlaylist.getTracks()) {
            gm.scheduler.queue(track);
        }
    }

    @Override
    public void noMatches() {
        failed = true;
        msg = interaction.getRandomTemplatesFail();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        failed = true;
        msg = interaction.getRandomTemplatesPermError();
    }

    public void connectToVoiceChannel(MessageReceivedEvent event){
        if(!event.getMessage().getAuthor().getConnectedVoiceChannels().get(0).isConnected()){
            if(event.getMessage().getAuthor().getConnectedVoiceChannels().size()>0){
                try {
                    event.getMessage().getAuthor().getConnectedVoiceChannels().get(0).join();
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getMsg() {
        return msg;
    }

    public boolean isFailed() {
        return failed;
    }
}
