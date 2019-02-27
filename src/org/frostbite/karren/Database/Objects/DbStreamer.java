/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database.Objects;

import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

public class DbStreamer {
    private int streamerID;
    private long guildID;
    private long userID;

    public DbStreamer() {

    }

    public DbStreamer(int streamerID, long guildID, long userID) {
        this.streamerID = streamerID;
        this.guildID = guildID;
        this.userID = userID;
    }

    public int getStreamerID() {
        return streamerID;
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void update(){
        if(Karren.conf.getAllowSQLRW()) {
            String sql = "UPDATE GuildUser SET GuildID=?, UserID=? WHERE StreamerID=?";
            Yank.execute(sql, new Object[]{guildID, userID, streamerID});
        }
    }
}
