/*
 * Copyright (c) 2020 Owen Bennett.
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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.frostbite.karren.Interactions.Interaction;

public class AudioResultHandler implements AudioLoadResultHandler {
    MessageReceivedEvent event;
    Interaction interaction;
    GuildMusicManager gm;
    String msg;
    boolean isPlaylist = false;
    int trackCount = 0;
    AudioTrack queuedTrack = null;
    long startAt = 0L;

    boolean failed = false;

    public AudioResultHandler(MessageReceivedEvent event, Interaction interaction, GuildMusicManager gm, String msg) {
        this.event = event;
        this.interaction = interaction;
        this.gm = gm;
        this.msg = msg;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        failed = gm.connectToVoiceChannel(event);
        audioTrack.setPosition(startAt);
        gm.scheduler.queue(audioTrack);
        queuedTrack = audioTrack;
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        failed = gm.connectToVoiceChannel(event);
        isPlaylist = true;
        trackCount = audioPlaylist.getTracks().size();
        for (AudioTrack track : audioPlaylist.getTracks()) {
            gm.scheduler.queue(track);
        }
    }

    @Override
    public void noMatches() {
        failed = true;
        msg = interaction.getRandomTemplate("fail").getTemplate();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        failed = true;
        msg = interaction.getRandomTemplate("permission").getTemplate();
    }

    public String getMsg() {
        return msg;
    }

    public void setStartAt(long startAt){
        this.startAt = startAt;
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
