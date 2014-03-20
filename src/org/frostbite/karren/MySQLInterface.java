package org.frostbite.karren;

import java.sql.*;
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
        sqlport = (Integer)botConf.getConfigPayload("sqlport");
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
    prepQuery usable types:
        0 - User queries(parting operations, fave handler)
        1 - CRaZyPANTS Radio interface
        2 - Retrieve song faves
     */
    public int prepQuery(int type, String mod, String[] args) throws SQLException {
        int result = 0;
        resetSQL();
        switch(type){
            case 0:
                result = prepForUser(mod, args);
                break;
            case 1:
                break;
            case 2:
                break;
        }
        return result;
    }
    /*
    USER OPERATIONS
     */
    public ArrayList<Object> getUserData(String nick) throws SQLException {
        ArrayList<Object> result = null;
        if(isNewUser(nick))
            makeUser(nick);
        query = "SELECT * FROM users WHERE user= ?";
        sqlPayload.add(nick);
        search = true;
        pstNeeded = true;
        result = executeQuery();
        return result;
    }
    private int prepForUser(String mod, String[] args) throws SQLException {
        Date date = new Date();
        ArrayList<Object> userData = getUserData(args[0]);
        int ready = 0;
        switch(mod.toLowerCase()){
            case "return":
                if(Boolean.parseBoolean((String)userData.get(1))){
                    query = "UPDATE users SET botpart=false WHERE user= ?";
                    sqlPayload.add(args[0]);
                    search = false;
                    pstNeeded = true;
                    ready = 1;
                } else {
                    ready = 2;
                }
                break;
            case "part":
                if(!(Boolean.parseBoolean((String)userData.get(1)))){
                    resetSQL();
                    query = "UPDATE users SET (botpart, timepart) VALUES (true, ?) WHERE user= ?";
                    sqlPayload.add(String.valueOf(date.getTime()));
                    sqlPayload.add(args[0]);
                    search = false;
                    pstNeeded = true;
                    ready = 1;
                } else {
                    ready = 2;
                }
                break;
        }
        return ready;
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
