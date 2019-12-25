/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.AudioPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private AudioTrack lastTrack = null;
    private Guild guild;
    private TextChannel announceChannel;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = guild;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack(boolean ended) {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player
        AudioTrack newSong = null;
        if(isShuffle && queue.size()>0) {
            Random rng = new Random();
            Object[] songs = queue.toArray();
            newSong = (AudioTrack) songs[rng.nextInt(songs.length)];
            queue.remove(newSong);
        }
        if(isRepeat && ended && newSong==null){
            newSong = lastTrack;
        }
        if(newSong==null){
            newSong = queue.poll();
        }
        player.startTrack(newSong, false);
        if (queue.size() == 0 && player.getPlayingTrack() == null) {
            player.destroy();
            setShuffle(false);
            setRepeat(false);
            guild.getAudioManager().closeAudioConnection();
        } else {
            assert newSong != null;
            announceChannel.sendMessage("Starting playback of \"" + newSong.getInfo().title + "\"").queue();
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void stopQueue() {
        player.stopTrack();
        player.destroy();
        queue.clear();
        guild.getAudioManager().closeAudioConnection();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            lastTrack = track;
            nextTrack(true);
        }

        if (endReason == AudioTrackEndReason.STOPPED) {
            announceChannel.sendMessage("Stopped playback").queue();
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        announceChannel.sendMessage("Playback has been paused").queue();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        announceChannel.sendMessage("Playback has been resumed!").queue();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        announceChannel.sendMessage("Playback error: " + exception.getLocalizedMessage()).queue();
    }

    public boolean isPlaying() {
        return player.getPlayingTrack() != null && !player.isPaused();
    }

    public boolean isSchedulerActive() {
        return player.getPlayingTrack() != null || queue.size() > 0;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public Guild getGuild() {
        return guild;
    }

    public TextChannel getAnnounceChannel() {
        return announceChannel;
    }

    public void setAnnounceChannel(TextChannel announceChannel) {
        this.announceChannel = announceChannel;
    }
}
