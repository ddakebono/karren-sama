/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Song {
    private String songName;
    private long lastPlayed;
    private int playCount;
    private int favCount;
    private int songID;
    private long songStartTime;
    private long songEndTime;
    private long lastSongDuration;
    private boolean isDurationLocked;

    public Song(String songName){
        this.songName = songName;
        Date date = new Date();
        this.songStartTime = date.getTime();
    }
    public Song(String songName, int playCount, int favCount, boolean isDurationLocked, String lastPlayed, long lastSongDuration){
        this.songName = songName;
        this.playCount = playCount;
        this.favCount = favCount;
        this.isDurationLocked = isDurationLocked;
        this.lastSongDuration = lastSongDuration;
        this.lastPlayed = getEpochTimeFromDateTime(lastPlayed);
    }
    public void setFieldsFromSQL(ArrayList<Object> results){
        lastPlayed = (Long)results.get(2);
        playCount = (int)results.get(3);
        favCount = (int)results.get(4);
        lastSongDuration = Long.valueOf(results.get(5).toString());
        isDurationLocked = (boolean)results.get(6);
    }
    public long getSongDuration(){
        long result = songEndTime-songStartTime;
        if(((result-lastSongDuration)>=-2000 && (result-lastSongDuration)<=2000) && !isDurationLocked)
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
    public long getLastPlayedRaw(){return lastPlayed;}
    public String getLastPlayed(){return getDateTimeFromEpoch(lastPlayed);}
    public int getPlayCount(){return playCount;}
    public int getFavCount(){return favCount;}
    public int getSongID(){return songID;}
    public long getLastSongDuration(){return lastSongDuration;}
    public String getDateTimeFromEpoch(Long epoch){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epoch);
        return new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss").format(cal.getTime());
    }
    public long getEpochTimeFromDateTime(String dateTime){
        String[] dateTimeSplit = dateTime.split("@");
        String[] dateSplit = dateTimeSplit[0].split("-");
        String[] timeSplit = dateTimeSplit[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Integer.parseInt(dateSplit[0].trim()));
        cal.set(Calendar.DATE, Integer.parseInt(dateSplit[1].trim()));
        cal.set(Calendar.YEAR, Integer.parseInt(dateSplit[2].trim()));
        cal.set(Calendar.HOUR, Integer.parseInt(timeSplit[0].trim()));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1].trim()));
        cal.set(Calendar.SECOND, Integer.parseInt(timeSplit[2].trim()));
        return cal.getTimeInMillis();
    }
}
