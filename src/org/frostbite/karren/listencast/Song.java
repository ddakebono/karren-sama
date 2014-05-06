/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import java.util.ArrayList;
import java.util.Date;

public class Song {
    private String songName;
    private String lastPlayed;
    private int playCount;
    private int favCount;
    private int songID;
    private long songStartTime;
    private long songEndTime;
    private long lastSongDuration;
    private boolean isDurationLocked;
    private Date date = new Date();
    public Song(String songName){
        this.songName = songName;
        this.songStartTime = date.getTime();
    }
    public void setFieldsFromSQL(ArrayList<Object> results){
        lastPlayed = (String)results.get(2);
        playCount = (int)results.get(3);
        favCount = (int)results.get(4);
        lastSongDuration = Long.valueOf(results.get(5).toString());
        isDurationLocked = (boolean)results.get(6);
    }
    public long getSongDuration(){
        long result = songEndTime-songStartTime;
        if(((result-lastSongDuration)>=-2000 && (result-lastSongDuration)<=2000) || isDurationLocked)
            result = lastSongDuration;
        return result;
    }
    public boolean isDurationLocked(){
        return isDurationLocked;
    }
    public void songEnded(){
        Date date = new Date();
        songEndTime = date.getTime();
    }
    public long getSongStartTime(){return songStartTime;}
    public void setSongID(int songID){
        this.songID = songID;
    }
    public String getSongName(){return songName;}
    public String getLastPlayed(){return lastPlayed;}
    public int getPlayCount(){return playCount;}
    public int getFavCount(){return favCount;}
    public int getSongID(){return songID;}
    public long getLastSongDuration(){return lastSongDuration;}
}
