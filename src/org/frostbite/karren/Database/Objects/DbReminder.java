package org.frostbite.karren.Database.Objects;

import java.sql.Timestamp;

/**
 * Created by ddakebono on 6/27/2017.
 */
public class DbReminder {
    public int reminderID;
    public String authorID;
    public String targetID;
    public Timestamp reminderTime;
    public String message;
    public boolean nextMessageReminder;

    public DbReminder(int reminderID, String authorID, String targetID, Timestamp reminderTime, String message, boolean nextMessageReminder) {
        this.reminderID = reminderID;
        this.authorID = authorID;
        this.targetID = targetID;
        this.reminderTime = reminderTime;
        this.message = message;
        this.nextMessageReminder = nextMessageReminder;
    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public Timestamp getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Timestamp reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNextMessageReminder() {
        return nextMessageReminder;
    }

    public void setNextMessageReminder(boolean nextMessageReminder) {
        this.nextMessageReminder = nextMessageReminder;
    }

    public void update(){

    }

}
