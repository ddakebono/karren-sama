/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by frostbite on 3/27/14.
 */
public class Song {
    private String songName;
    private String lastPlayed;
    private int playCount;
    private int favCount;
    private int songID;
    public Song(String songName){
        this.songName = songName;
    }
    public void setFieldsFromSQL(ArrayList<Object> results){
        lastPlayed = (String)results.get(2);
        playCount = (int)results.get(3);
        favCount = (int)results.get(4);
    }
    public void setSongID(int songID){
        this.songID = songID;
    }
    public String getSongName(){return songName;}
    public String getLastPlayed(){return lastPlayed;}
    public int getPlayCount(){return playCount;}
    public int getFavCount(){return favCount;}
    public int getSongID(){return songID;}
}
