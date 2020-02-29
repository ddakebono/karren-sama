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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Playlist {

    ArrayList<PlaylistSong> playlist = new ArrayList<>();
    PlaylistSong[] suspendedPlaylist;
    int currentSongIndex = 0;
    int suspendedPlaylistIndex = 0;
    PlaylistSong currentShuffleSong;

    //playlist settings
    boolean repeatSong = false;
    boolean repeatPlaylist = false;
    boolean shuffle = false;

    public void offer(AudioTrack track){
        playlist.add(new PlaylistSong(track));
    }

    public boolean isSuspended(){
        if(suspendedPlaylist!=null)
            return suspendedPlaylist.length>0;
        return false;
    }

    public AudioTrack poll(int skip){
        currentSongIndex+=skip;
        if(!repeatSong)
            currentSongIndex++;
        if(playlist.size()>currentSongIndex) {
            if (!shuffle){
                return playlist.get(currentSongIndex).track;
            }else{
                Random random = new Random();
                Object[] unplayedSongs = playlist.stream().filter(x -> !x.hasPlayed).toArray();
                PlaylistSong song = (PlaylistSong) unplayedSongs[random.nextInt(unplayedSongs.length)];
                song.hasPlayed = true;
                currentShuffleSong = song;
                return song.track;
            }
        } else if(repeatPlaylist && playlist.size()>0) {
            resetSongsPlayed();
            currentSongIndex = 0;
            return poll(0);
        }
        return null;
    }

    public void suspend(){
        suspendedPlaylist = playlist.toArray(new PlaylistSong[0]);
        suspendedPlaylistIndex = currentSongIndex;
        clear();
    }

    public boolean resume(){
        if(isSuspended()) {
            playlist.addAll(Arrays.asList(suspendedPlaylist));
            currentSongIndex = suspendedPlaylistIndex;
            suspendedPlaylist = null;
            suspendedPlaylistIndex = 0;
            return true;
        }
        return false;
    }

    public void resetSongsPlayed(){
        for(PlaylistSong song : playlist){
            song.hasPlayed = false;
        }
    }

    public ArrayList<PlaylistSong> getSongs(){
        return playlist;
    }

    public void remove(int index){
        playlist.remove(index);
    }

    public void clear(){
        playlist.clear();
        currentSongIndex = 0;
    }

    public int getSize(){
        return playlist.size();
    }

    public boolean toggleRepeatPlaylist(){
        repeatPlaylist = !repeatPlaylist;
        return repeatPlaylist;
    }

    public boolean toggleRepeat(){
        repeatSong = !repeatSong;
        return repeatSong;
    }

    public boolean toggleShuffle(){
        shuffle = !shuffle;
        return shuffle;
    }

    public int getCurrentSongIndex(){
        if(shuffle)
            return playlist.indexOf(currentShuffleSong);
        return currentSongIndex;
    }

    public int getPlayedSongs(){
        if(shuffle){
            long playedSongs = playlist.stream().filter(x -> x.hasPlayed).count();
            return Math.toIntExact(playedSongs);
        }
        return currentSongIndex;
    }
}
