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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlaylistSong {
    public AudioTrack track;
    public boolean hasPlayed = false;

    public PlaylistSong(AudioTrack track){
        this.track = track;
    }
}
