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

public class DbUser {
    private String userID;
    private Timestamp timeLeft;

    public DbUser(){
    }

    public DbUser(String userID, Timestamp timeLeft) {
        this.userID = userID;
        this.timeLeft = timeLeft;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Timestamp timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void update(){
        String sql = "UPDATE User SET TimeLeft=? WHERE UserID=?";
        Yank.execute(sql, new Object[]{timeLeft, userID});
    }
}
