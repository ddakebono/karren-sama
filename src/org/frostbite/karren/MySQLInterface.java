/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import org.frostbite.karren.listencast.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MySQLInterface {
    private String sqlhost;
    private String sqluser;
    private String sqldb;
    private String sqlpass;
    private int sqlport;
    private String query;
    private boolean search;
    private boolean pstNeeded;
    private String overrideDB;
    private boolean rwEnabled;
    private ArrayList<String> sqlPayload = new ArrayList<>();
    /*
    CONSTRUCTORS
     */
    public MySQLInterface(){
        sqldb = Karren.conf.getSqldb();
        sqlhost = Karren.conf.getSqlhost();
        sqlpass = Karren.conf.getSqlpass();
        sqlport = Integer.parseInt(Karren.conf.getSqlport());
        sqluser = Karren.conf.getSqluser();
        rwEnabled = Boolean.parseBoolean(Karren.conf.getAllowSQLRW());
    }
    /*
    UTILITY OPERATIONS
     */
    private void resetSQL(){
        sqlPayload.clear();
        query = null;
        search = false;
        pstNeeded = false;
        overrideDB = null;
    }
    public boolean isNewUser(String nick){
        ArrayList<String> savedUsers = new ArrayList<>();
        boolean userNew = true;
        try{
            query = "SELECT user FROM users";
            search = true;
            pstNeeded = false;
            ArrayList<Object> usrTemp = executeQuery();
            for (Object user : usrTemp) {
                savedUsers.add((String) user);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            Karren.log.error("Error in SQL Operation:", e);
        }
        if(savedUsers.size() > 0){
            for(String curUser : savedUsers){
                if(nick.equalsIgnoreCase(curUser)){
                    userNew = false;
                }
            }
        }
        return userNew;
    }
    public void makeUser(String nick){
        resetSQL();
        query = "INSERT INTO users (ircuserid, user, botpart, timepart) VALUES (null, ?, false, 0)";
        sqlPayload.add(nick);
        try {
            search = false;
            pstNeeded = true;
            executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    USER OPERATIONS
     */
    public ArrayList<Object> getUserData(String nick) throws SQLException {
        ArrayList<Object> result;
        if(isNewUser(nick))
            makeUser(nick);
        resetSQL();
        query = "SELECT * FROM users WHERE user= ?";
        sqlPayload.add(nick);
        search = true;
        pstNeeded = true;
        result = executeQuery();
        return result;
    }
    /*
    Expected arguments:
        1: Nick of user
     */
    public void userOperation(String mod, String[] args) throws SQLException {
        Date date = new Date();
        ArrayList<Object> userData = getUserData(args[0]);
        resetSQL();
        switch(mod.toLowerCase()){
            case "return":
                if(Boolean.parseBoolean(String.valueOf(userData.get(2)))){
                    query = "UPDATE users SET botpart=false WHERE user= ?";
                    sqlPayload.add(args[0]);
                    search = false;
                    pstNeeded = true;
                    executeQuery();
                }
                break;
            case "part":
                if(!(Boolean.parseBoolean(String.valueOf(userData.get(2))))){
                    query = "UPDATE users SET botpart=true, timepart= ? WHERE user= ?";
                    sqlPayload.add(String.valueOf(date.getTime()));
                    sqlPayload.add(args[0]);
                    search = false;
                    pstNeeded = true;
                    executeQuery();
                }
                break;
        }
    }
    /*
    RADIO OPERATIONS
     */
    public ArrayList<Object> getUserFaves(Song song) throws SQLException {
        ArrayList<Object> result;
        resetSQL();
        query = "SELECT User FROM userfaves WHERE SongID=?";
        sqlPayload.add(String.valueOf(song.getSongID()));
        search = true;
        pstNeeded = true;
        result = executeQuery();
        return result;
    }
    public boolean addFave(String user, Song song) throws SQLException {
        resetSQL();
        query = "SELECT * FROM userfaves WHERE User=? AND SongID=?";
        sqlPayload.add(user);
        sqlPayload.add(String.valueOf(song.getSongID()));
        search = true;
        pstNeeded = true;
        ArrayList<Object> returned = executeQuery();
        if(returned.size()==0){
            resetSQL();
            query = "INSERT INTO userfaves(ID, User, SongID) VALUES (null, ?, ?)";
            sqlPayload.add(user);
            sqlPayload.add(String.valueOf(song.getSongID()));
            search = false;
            pstNeeded = true;
            executeQuery();
            resetSQL();
            query = "UPDATE songdb SET FavCount=FavCount+1 WHERE id=?";
            sqlPayload.add(String.valueOf(song.getSongID()));
            search = false;
            pstNeeded = true;
            executeQuery();
            return true;
        } else {
            return false;
        }
    }
    public void updateDJActivity(String curDJ, String streamName) throws SQLException {
        if(curDJ.length()==0){
            //Setting no DJ to active(Stream offair)
            resetSQL();
            query = "UPDATE radio_dj SET active=false";
            search = false;
            pstNeeded = false;
            executeQuery();
        } else {
            resetSQL();
            query = "INSERT INTO radio_dj(ID,displayName,connectName,streamName,djPicture,active) VALUES (null, ?, ?, ?, 'default', true) ON DUPLICATE KEY UPDATE active=true, streamName=?";
            sqlPayload.add(curDJ);
            sqlPayload.add(curDJ);
            sqlPayload.add(streamName);
            sqlPayload.add(streamName);
            search = false;
            pstNeeded = true;
            executeQuery();
        }
    }
    public void updateSongData(Song lastsong) throws SQLException{
        if(!lastsong.isDurationLocked() || lastsong.getLastSongDuration() == 0) {
            resetSQL();
            if (lastsong.getLastSongDuration() == lastsong.getSongDuration() && lastsong.getLastSongDuration() > 0) {
                query = "UPDATE songdb SET songduration=?, DurationLock=1 WHERE id=?";
                Karren.log.debug("Setting \"" + lastsong.getSongName() + "\" duration lock to true");
            } else {
                query = "UPDATE songdb SET songduration=? WHERE id=?";
            }
            sqlPayload.add(String.valueOf(lastsong.getSongDuration()));
            sqlPayload.add(String.valueOf(lastsong.getSongID()));
            search = false;
            pstNeeded = true;
            executeQuery();
        }
    }
    public void updateRadioDatabase(Song song) throws SQLException {
        if(rwEnabled) {
            resetSQL();
            ArrayList<Object> returned;
            Long curTime;
            query = "SELECT ID FROM songdb WHERE SongTitle = ?";
            pstNeeded = true;
            search = true;
            sqlPayload.add(song.getSongName());
            returned = executeQuery();
            resetSQL();
            if (returned.size() > 0) {
                song.setSongID((int) returned.get(0));
                returned.clear();
                query = "SELECT * FROM songdb WHERE ID= ?";
                sqlPayload.add(String.valueOf(song.getSongID()));
                search = true;
                pstNeeded = true;
                returned = executeQuery();
                song.setFieldsFromSQL(returned);
            } else {
                returned.clear();
                song.setSongID(0);
                returned.add(null);
                returned.add(null);
                returned.add((long) 0);
                returned.add(0);
                returned.add(0);
                returned.add(0);
                returned.add(false);
                song.setFieldsFromSQL(returned);
            }
            returned.clear();
            Date date = new Date();
            if (song.getSongID() == 0) {
                //Adding song to DB and getting new ID for song
                resetSQL();
                query = "INSERT INTO songdb (ID, SongTitle, LPTime, PlayCount, FavCount, SongDuration, DurationLock) VALUES (null, ?, ?, 1, 0, 0, false)";
                sqlPayload.add(song.getSongName());
                curTime = date.getTime();
                sqlPayload.add(curTime.toString());
                search = false;
                pstNeeded = true;
                executeQuery();
                resetSQL();
                query = "SELECT ID FROM songdb WHERE SongTitle = ?";
                sqlPayload.add(song.getSongName());
                search = true;
                pstNeeded = true;
                returned = executeQuery();
                if (returned.size() > 0) {
                    song.setSongID((int) returned.get(0));
                }
                resetSQL();
            } else {
                resetSQL();
                //Update info for song
                query = "UPDATE songdb SET LPTime= ?, PlayCount=PlayCount+1 WHERE ID=?";
                curTime = date.getTime();
                sqlPayload.add(curTime.toString());
                sqlPayload.add(String.valueOf(song.getSongID()));
                search = false;
                pstNeeded = true;
                executeQuery();
            }
            Karren.log.info("Now playing: " + song.getSongName() + ":" + song.getSongID() + ":" + song.getPlayCount());
        }
    }
    /*
    SQL OPERATIONS
     */
    public ArrayList<Object> executeQuery() throws SQLException {
        if(Boolean.parseBoolean(Karren.conf.getAllowSQLRW())) {
            String targetDB = sqldb;
            ArrayList<Object> result = new ArrayList<>();
            if (overrideDB != null)
                targetDB = overrideDB;
            Connection run = DriverManager.getConnection("jdbc:mysql://" + sqlhost + ":" + sqlport + "/" + targetDB + "?useUnicode=true&characterEncoding=UTF-8", sqluser, sqlpass);
            PreparedStatement pst;
            ResultSet rs;
            pst = run.prepareStatement(query);
            if (pstNeeded) {
                for (int i = 0; i < sqlPayload.size(); i++) {
                    pst.setString(i + 1, sqlPayload.get(i));
                }
            }
            if (search) {
                rs = pst.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                int cCount = md.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= cCount; i++) {
                        result.add(rs.getObject(i));
                    }
                }
            }
            if (!search)
                pst.execute();
            run.close();
            return result;
        }
        return null;
    }
}
