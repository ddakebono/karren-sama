/*
 * Copyright (c) 2018 Owen Bennett.
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

import java.sql.Timestamp;

public class DbUser {
    private long userID = 0;
    private Timestamp timeLeft;
    private String vrcUserID;
    private int points;

    public DbUser(){
    }

    public DbUser(int userID, Timestamp timeLeft, String vrcUserID, int points) {
        this.userID = userID;
        this.timeLeft = timeLeft;
        this.vrcUserID = vrcUserID;
        this.points = points;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getVrcUserID() {
        return vrcUserID;
    }

    public void setVrcUserID(String vrcUserID) {
        this.vrcUserID = vrcUserID;
    }

    public Timestamp getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Timestamp timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void update(){
        if(Karren.conf.getAllowSQLRW()) {
            String sql = "UPDATE User SET TimeLeft=?, VRCUserID=?, Points=? WHERE UserID=?";
            Yank.execute(sql, new Object[]{timeLeft, vrcUserID, points, userID});
        }
    }
}
