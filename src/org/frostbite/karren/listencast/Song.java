/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listencast;

import org.frostbite.karren.Database.Models.tables.records.SongdbRecord;
import org.frostbite.karren.Karren;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.BooleanSupplier;

public class Song{
    private String songName;
    private long lastPlayed;
    private int playCount;
    private int favCount;
    private int songID;
    private long songStartTime;
    private long songEndTime;
    private long lastSongDuration;
    private boolean isDurationLocked;
    private SongdbRecord sqlSong;

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

    void getFieldsFromSQL(){
        sqlSong = Karren.bot.getSql().getSong(songName);
        songID = sqlSong.getId();
        lastPlayed = new Timestamp(new Date().getTime()).getTime();
        playCount = sqlSong.getPlaycount();
        favCount = sqlSong.getFavcount();
        lastSongDuration = sqlSong.getSongduration();
        isDurationLocked = Boolean.parseBoolean(sqlSong.getDurationlock().toString());
        sqlSong.setLptime(lastPlayed);
        sqlSong.update();
    }

    long getSongDuration(){
        long result = songEndTime-songStartTime;
        if(((result-lastSongDuration)>=-2000 && (result-lastSongDuration)<=2000) && !isDurationLocked)
            result = lastSongDuration;
        return result;
    }
    public void addFave(){
        favCount++;
    }
    boolean isDurationLocked(){
        return isDurationLocked;
    }
    void songEnded(){
        Date date = new Date();
        songEndTime = date.getTime();
        if(sqlSong!=null) {
            sqlSong.setPlaycount(playCount + 1);
            if (!isDurationLocked || lastSongDuration == 0) {
                if (lastSongDuration == getSongDuration() && lastSongDuration > 0) {
                    sqlSong.setSongduration(getSongDuration());
                    sqlSong.setDurationlock((byte) 1);
                } else {
                    sqlSong.setSongduration(getSongDuration());
                }
            }
            sqlSong.setLptime(new Timestamp(new Date().getTime()).getTime());
            sqlSong.setFavcount(favCount);
            sqlSong.update();
        }
    }
    long getSongStartTime(){return songStartTime;}
    public void setSongID(int songID){
        this.songID = songID;
    }
    public String getSongName(){return songName;}
    public long getLastPlayedRaw(){return lastPlayed;}
    String getLastPlayed(){
        if(lastPlayed == 0)
            return "Never";
        else
            return getDateTimeFromEpoch(lastPlayed);
    }

    public SongdbRecord getSqlSong() {
        return sqlSong;
    }

    int getPlayCount(){return playCount;}
    int getFavCount(){return favCount;}
    public int getSongID(){return songID;}
    public long getLastSongDuration(){return lastSongDuration;}
    private String getDateTimeFromEpoch(Long epoch){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epoch);
        return new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss").format(cal.getTime());
    }
    private long getEpochTimeFromDateTime(String dateTime){
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
