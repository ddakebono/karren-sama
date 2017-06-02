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
    private boolean notMapped = false;

    public DbGuildUser(){}

    public int getGuildUserID() {
        return guildUserID;
    }

    public void setGuildUserID(int guildUserID) {
        this.guildUserID = guildUserID;
    }

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

    public boolean isNotMapped() {
        return notMapped;
    }

    public void setNotMapped(boolean notMapped) {
        this.notMapped = notMapped;
    }

    public void update(){
        if(!notMapped) {
            String sql = "UPDATE GuildUser SET RollTimeout=?, IgnoreCommands=? WHERE GuildUserID=?";
            Yank.execute(sql, new Object[]{rollTimeout, ignoreCommands, guildUserID});
        }
    }
}

