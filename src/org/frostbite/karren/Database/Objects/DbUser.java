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
    private long userID;
    private Timestamp timeLeft;
    private String djName;
    private String djPicture;
    private boolean djActive;
    private String djStreamName;

    public DbUser(){
    }

    public DbUser(int userID, Timestamp timeLeft, String djName, String djPicture, boolean djActive, String djStreamName) {
        this.userID = userID;
        this.timeLeft = timeLeft;
        this.djName = djName;
        this.djPicture = djPicture;
        this.djActive = djActive;
        this.djStreamName = djStreamName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Timestamp getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Timestamp timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getDjName() {
        return djName;
    }

    public void setDjName(String djName) {
        this.djName = djName;
    }

    public String getDjPicture() {
        return djPicture;
    }

    public void setDjPicture(String djPicture) {
        this.djPicture = djPicture;
    }

    public boolean isDjActive() {
        return djActive;
    }

    public void setDjActive(boolean djActive) {
        this.djActive = djActive;
    }

    public String getDjStreamName() {
        return djStreamName;
    }

    public void setDjStreamName(String djStreamName) {
        this.djStreamName = djStreamName;
    }

    public void update(){
        String sql = "UPDATE User SET TimeLeft=?, DJName=?, DJPicture=?, DJActive=?, DJStreamName=? WHERE UserID=?";
        Yank.execute(sql, new Object[]{timeLeft, djName, djPicture, djActive, djStreamName, userID});
    }
}
