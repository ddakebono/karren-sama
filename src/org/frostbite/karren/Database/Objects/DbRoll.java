package org.frostbite.karren.Database.Objects;

import java.sql.Timestamp;

/**
 * Created by ddakebono on 5/31/2017.
 */
public class DbRoll {
    private int rollID;
    private int userID;
    private String guildID;
    private Timestamp rollTimeout;

    public DbRoll(int rollID, int userID, String guildID, Timestamp rollTimeout) {
        this.rollID = rollID;
        this.userID = userID;
        this.guildID = guildID;
        this.rollTimeout = rollTimeout;
    }

    public int getRollID() {
        return rollID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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
}
