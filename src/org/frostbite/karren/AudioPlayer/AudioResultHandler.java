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
    boolean isPlaylist = false;
    int trackCount = 0;
    AudioTrack queuedTrack = null;

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
        queuedTrack = audioTrack;
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        connectToVoiceChannel(event);
        isPlaylist = true;
        trackCount = audioPlaylist.getTracks().size();
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
        if(event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel() != null){
            if (!event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().isConnected()) {
                try {
                    event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().join();
                } catch (MissingPermissionsException e) {
                        e.printStackTrace();
                }
            }
        } else {
            failed = true;
            msg = "Hold on a sec, you're not in a voice channel!";
        }
    }

    public String getMsg() {
        return msg;
    }

    public boolean isPlaylist() {
        return isPlaylist;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public AudioTrack getQueuedTrack() {
        return queuedTrack;
    }

    public boolean isFailed() {
        return failed;
    }
}
