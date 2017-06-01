/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database.Objects;

import org.knowm.yank.Yank;

import java.sql.Timestamp;

public class DbWordcount {
    private int wordID;
    private String word;
    private int count;
    private Timestamp countStarted;

    public DbWordcount(){}

    public DbWordcount(int wordID, String word, int count, Timestamp countStarted) {
        this.wordID = wordID;
        this.word = word;
        this.count = count;
        this.countStarted = countStarted;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Timestamp getCountStarted() {
        return countStarted;
    }

    public void setCountStarted(Timestamp countStarted) {
        this.countStarted = countStarted;
    }

    public void incrementCount(){
        count++;
    }

    public void update(){
        String sql = "UPDATE WordCounts SET Count=? WHERE WordID=?";
        Yank.execute(sql, new Object[]{count, wordID});
    }
}
