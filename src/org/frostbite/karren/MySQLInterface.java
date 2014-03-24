package org.frostbite.karren;

import org.frostbite.karren.listencast.ListenCast;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by frostbite on 19/03/14.
 */
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
    private ArrayList<String> sqlPayload = new ArrayList<>();
    /*
    CONSTRUCTORS
     */
    public MySQLInterface(String sqlhost, String sqluser, String sqldb, String sqlpass, int sqlport){
        this.sqldb = sqldb;
        this.sqlhost = sqlhost;
        this.sqlpass = sqlpass;
        this.sqlport = sqlport;
        this.sqluser = sqluser;
    }
    public MySQLInterface(BotConfiguration botConf){
        sqldb = (String)botConf.getConfigPayload("sqldb");
        sqlhost = (String)botConf.getConfigPayload("sqlhost");
        sqlpass = (String)botConf.getConfigPayload("sqlpass");
        sqlport = Integer.parseInt((String)botConf.getConfigPayload("sqlport"));
        sqluser = (String)botConf.getConfigPayload("sqluser");
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
        ArrayList<String> savedUsers = new ArrayList<String>();
        boolean userNew = true;
        try{
            query = "SELECT user FROM users";
            search = true;
            pstNeeded = false;
            ArrayList<Object> usrTemp = executeQuery();
            for(int i=0; i<usrTemp.size(); i++){
                savedUsers.add((String) usrTemp.get(i));
            }
        } catch(SQLException e) {
            e.printStackTrace();
            Logging.log(e.getMessage(), true);
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
        query = "INSERT INTO users (user, botpart, timepart, timeWasted) VALUES (?, false, 0, 0)";
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
        ArrayList<Object> result = null;
        resetSQL();
        if(isNewUser(nick))
            makeUser(nick);
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
        int ready = 0;
        switch(mod.toLowerCase()){
            case "return":
                if((Boolean)userData.get(1)){
                    query = "UPDATE users SET botpart=false WHERE user= ?";
                    sqlPayload.add(args[0]);
                    search = false;
                    pstNeeded = true;
                    executeQuery();
                }
                break;
            case "part":
                if(!((Boolean)userData.get(1))){
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
    public void updateRadioPage(ListenCast lc) throws SQLException {
        resetSQL();
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Object> returned;
        String curTime = "00-00-0000 00:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss");
        query = "SELECT ID FROM SongDB WHERE SongTitle = ?";
        pstNeeded = true;
        search = true;
        sqlPayload.add(lc.getNpSong());
        returned = executeQuery();
        resetSQL();
        if(returned.size()>0){
            lc.setSongID((int)returned.get(0));
            returned.clear();
            query = "SELECT * FROM SongDB WHERE ID= ?";
            sqlPayload.add(String.valueOf(lc.getSongID()));
            search = true;
            pstNeeded = true;
            returned = executeQuery();
            GlobalVars.lpTime = (String)returned.get(2);
            GlobalVars.songPlayedAmount = (int)returned.get(3);
            GlobalVars.songFavCount = (int)returned.get(4);
            dataForSQL.clear();
        } else {
            GlobalVars.songID = 0;
            GlobalVars.lpTime = "Never";
            GlobalVars.songFavCount = 0;
            GlobalVars.songPlayedAmount = 1;
        }
        returned.clear();
        if(GlobalVars.songID == 0){
            //Adding song to DB and getting new ID for song
            statmentBuild = "INSERT INTO SongDB (ID, SongTitle, LPTime, PlayCount, FavCount) VALUES (null, ?, ?, 1, 0)";
            dataForSQL.add(GlobalVars.npSong);
            curTime = getCurDate(curTime, dateFormat);
            dataForSQL.add(curTime);
            runCommand(statmentBuild, dataForSQL, false, true, null);
            GlobalVars.songChange = false;
            dataForSQL.clear();
            statmentBuild = "SELECT ID FROM SongDB WHERE SongTitle = ?";
            dataForSQL.add(GlobalVars.npSong);
            returned = runCommand(statmentBuild, dataForSQL, true, true, null);
            if(returned.size()>0){
                GlobalVars.songID = (int) returned.get(0);
            }
            returned.clear();
        } else {
            //Update info for song
            statmentBuild = "UPDATE SongDB SET LPTime= ?, PlayCount=PlayCount+1 WHERE ID=" + GlobalVars.songID;
            curTime = getCurDate(curTime, dateFormat);
            dataForSQL.add(curTime);
            runCommand(statmentBuild, dataForSQL, false, true, null);
            GlobalVars.songChange = false;
        }
        Logging.song(npSong + ":" + songID + ":" + GlobalVars.songPlayedAmount);
    }
    /*
    SQL OPERATIONS
     */
    public ArrayList<Object> executeQuery() throws SQLException {
        String targetDB = sqldb;
        ArrayList<Object> result = new ArrayList<Object>();
        if(overrideDB != null)
            targetDB = overrideDB;
        Connection run = DriverManager.getConnection("jdbc:mysql://" + sqlhost + ":" + sqlport + "/" + targetDB, sqluser, sqlpass);
        PreparedStatement pst;
        ResultSet rs = null;
        pst = run.prepareStatement(query);
        if(pstNeeded){
            for(int i=0; i<sqlPayload.size(); i++){
                pst.setString(i+1, sqlPayload.get(i));
            }
        }
        if(search){
            rs = pst.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int cCount = md.getColumnCount();
            while(rs.next()){
                for(int i=1; i<=cCount; i++){
                    result.add(rs.getObject(i));
                }
            }
        }
        if(!search)
            pst.execute();
        run.close();
        return result;
    }
}
