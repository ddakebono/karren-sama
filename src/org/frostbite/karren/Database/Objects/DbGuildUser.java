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

public class DbGuildUser {
    private int guildUserID;
    private long userID;
    private String guildID;
    private Timestamp rollTimeout;
    private boolean ignoreCommands;
    private int rollsSinceLastClear;
    private int totalRolls;
    private int winningRolls;
    private boolean notMapped = false;

    public DbGuildUser(){}

    public int getGuildUserID() {
        return guildUserID;
    }

    public void setGuildUserID(int guildUserID) {
        this.guildUserID = guildUserID;
    }

    public int getRollsSinceLastClear() {
        return rollsSinceLastClear;
    }

    public void setRollsSinceLastClear(int rollsSinceLastClear) {
        this.rollsSinceLastClear = rollsSinceLastClear;
    }

    public int getTotalRolls() {
        return totalRolls;
    }

    public void setTotalRolls(int totalRolls) {
        this.totalRolls = totalRolls;
    }

    public void incrementTotalRolls(){totalRolls++;}

    public void incrementRollsSinceLastClear() {rollsSinceLastClear++;}

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public Timestamp getRollTimeout() {
        return rollTimeout;
    }

    public void setRollTimeout(Timestamp rollTimeout) {
        this.rollTimeout = rollTimeout;
    }

    public boolean isIgnoreCommands() {
        return ignoreCommands;
    }

    public void setIgnoreCommands(boolean ignoreCommands) {
        this.ignoreCommands = ignoreCommands;
    }

    public int getWinningRolls() {
        return winningRolls;
    }

    public void setWinningRolls(int winningRolls) {
        this.winningRolls = winningRolls;
    }

    public void incrementWinningRolls(){
        this.winningRolls++;
    }

    public boolean isNotMapped() {
        return notMapped;
    }

    public void setNotMapped(boolean notMapped) {
        this.notMapped = notMapped;
    }

    public void update(){
        if(!notMapped) {
            String sql = "UPDATE GuildUser SET RollTimeout=?, IgnoreCommands=?, RollsSinceLastClear=?, TotalRolls=?, WinningRolls=? WHERE GuildUserID=?";
            Yank.execute(sql, new Object[]{rollTimeout, ignoreCommands, rollsSinceLastClear, totalRolls, winningRolls, guildUserID});
        }
    }
}

