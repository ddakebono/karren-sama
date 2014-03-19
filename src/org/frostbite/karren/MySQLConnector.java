/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MySQLConnector {
    private static String statmentBuild = "";
    private static KarrenBot bot;
    private static ArrayList<String> dataForSQL = new ArrayList<String>();
    public MySQLConnector(KarrenBot bot){
        this.bot = bot;
    }
	public static ArrayList<String> sqlPush(String type, String mod, Object[] data) throws IOException, SQLException{
        dataForSQL.clear();
        statmentBuild = "";
		ArrayList<String> result = new ArrayList<String>();
		switch(type){
			case "news":
				pushNews(mod, data);
				break;
			case "site":
				//pushSite(mod, data);
				break;
			case "radio":
				result.add(pushRadio(mod, data));
				break;
			case "stats":
				//pushStats(mod);
				break;
			case "user":
				result.addAll(pushUser(mod, data));
				break;
			case "hash":
				result.add(pushHash(mod, data));
				break;
			case "song":
				result.addAll(pushSong(mod, data));
				break;
			case "fave":
				result.addAll(getFave());
                break;
			default:
				result.add(null);
				break;
		}
		return result;
	}/*
	* pushSong - Handles all communication between the IRC bot and the internet radio database
	*
	* Gets song information and pushes fave information for a specific song, utilizes song IDs
	* which are selected upon the song being played.
	*
	*/
	private static ArrayList<String> pushSong(String mod ,Object[] data) throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Object> returned;
        String curTime = "00-00-0000 00:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss");
		if(GlobalVars.songChange){
			statmentBuild = "SELECT ID FROM SongDB WHERE SongTitle = ?";
			dataForSQL.add(GlobalVars.npSong);
			returned = runCommand(statmentBuild, dataForSQL, true, true, null);
			dataForSQL.clear();
			if(returned.size()>0){
				GlobalVars.songID = (int) returned.get(0);
				returned.clear();
				statmentBuild = "SELECT * FROM SongDB WHERE ID= ?";
				dataForSQL.add(String.valueOf(GlobalVars.songID));
				returned = runCommand(statmentBuild, dataForSQL, true, true, null);
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
			Logging.song(GlobalVars.npSong + ":" + GlobalVars.songID + ":" + GlobalVars.songPlayedAmount);
		}
		if(mod.equalsIgnoreCase("fave")){
			statmentBuild = "SELECT * FROM UserFaves WHERE SongID = ? AND User = ?";
			dataForSQL.add(String.valueOf(data[0]));
			dataForSQL.add(String.valueOf(data[1]));
			returned = runCommand(statmentBuild, dataForSQL, true, true, null);
			dataForSQL.clear();
			if(returned.size()<1){
				statmentBuild = "INSERT INTO UserFaves(ID, User, SongID) VALUES (null, ?, ?)";
				dataForSQL.add(String.valueOf(data[1]));
				dataForSQL.add(String.valueOf(data[0]));
				runCommand(statmentBuild, dataForSQL, false, true, null);
				dataForSQL.clear();
				statmentBuild = "UPDATE SongDB SET FavCount=FavCount+1 WHERE ID= ?";
				dataForSQL.add(String.valueOf(GlobalVars.songID));
				runCommand(statmentBuild, dataForSQL, false, true, null);
				result.add("1");
			} else {
				result.add("0");
			}
		}
		return result;
	}
	private static ArrayList<String> getFave() throws IOException, SQLException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Object> returned = new ArrayList<Object>();
		statmentBuild = "SELECT User FROM UserFaves WHERE SongID= ?";
		dataForSQL.add(String.valueOf(GlobalVars.songID));
		returned = runCommand(statmentBuild, dataForSQL, true, true, null);
		for(Object nick : returned){
			result.add((String)nick);
		}
		return result;
	}
	private static void pushNews(String mod, Object[] data) throws IOException{
        String curDate = "0000-00-00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");
		curDate = getCurDate(curDate, dateFormat);
		Logging.log(curDate, true);
		statmentBuild = "INSERT INTO news (author, post, id, header, date) VALUES ( ? , ? , null, 1, ?)";
		//adds author info
		dataForSQL.add(String.valueOf(data[0]));
		//Adds post
		dataForSQL.add(String.valueOf(data[1]));
		//Adds timestamp
		dataForSQL.add(curDate);
		try{
			runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
		} catch(SQLException e) {
			Logging.log(e.toString(), true);
		}
	}
	/*public static boolean pushSite(String mod, String[] data) throws IOException{
	*	boolean result = false;
	*	String statmentBuild = "";
	*	return false;
	}*/
    /*
    * pushHash - Handles the DJ-Hashcode system, this is used with the UTF8-Fixer program to allow(Sometimes) proper
    * Unicode metadata transmission to the site and IRC bot.
    *
    * Generates a hashcode based off of the DJ's username and a secret multiplier code set in the configs.
    *
    * Likes to be negitive but it doesn't effect the operation.
     */
	private static String pushHash(String mod, Object[] data) throws SQLException{
		ArrayList<Object> result;
		long hashTemp = 1;
		String resultHash = "";
		String hashStringComp = "";
		boolean hasHash = false;
		ArrayList<String> djList = new ArrayList<String>();
		statmentBuild = "SELECT * FROM `Radio-DJ`";
		result = runCommand(statmentBuild, dataForSQL, true, false, "sitebackend");
		for(int i=0; i<result.size(); i++){
			djList.add(String.valueOf(result.get(i)));
		}
		result.clear();
		hasHash = djList.contains(data[0]);
		if(!hasHash){
			//Generating new DJHash code
			for(int i=0; i<String.valueOf(data[0]).length(); i++){
				hashTemp = hashTemp * String.valueOf(data[0]).charAt(i);
			}
			hashTemp = hashTemp*(int)(bot.getBotConf().getConfigPayload("djhashgenkey"));
			char[] hashArray = String.valueOf(hashTemp).toCharArray();
			if(hashArray.length>8){
				for(int c=0; c<7; c++){
					hashStringComp = hashStringComp + Character.toString(hashArray[c]);
				}
				resultHash = hashStringComp;
			} else {
				resultHash = String.valueOf(hashTemp);
			}
			statmentBuild = "INSERT INTO `Radio-DJ`(DJName, DJHash) VALUES (?, ?)";
			dataForSQL.add((String)data[0]);
			dataForSQL.add(String.valueOf(resultHash));
			runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			dataForSQL.clear();
		
		} else {
			statmentBuild = "SELECT DJHash FROM `Radio-DJ` WHERE DJName= ?";
			dataForSQL.add((String)data[0]);
			result = runCommand(statmentBuild, dataForSQL, true, true, "sitebackend");
			resultHash = String.valueOf(result.get(0));
		}
		return resultHash;
	}
	private static String pushRadio(String mod, Object[] data) throws IOException{
		String result = "";
		ArrayList<Object> returned;
		//Updating now playing song
		if(mod.equalsIgnoreCase("GetSong")){
			statmentBuild = "SELECT NowPlaying FROM radio";
			try {
				returned = runCommand(statmentBuild, dataForSQL, true, false, "sitebackend");
				if(returned.size()>0)
					result = (String)returned.get(0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("Song")){
			statmentBuild = "SELECT Spot FROM lastplayed WHERE SongTitle=?";
			dataForSQL.add(GlobalVars.npSong);
			try {
				returned = runCommand(statmentBuild, dataForSQL, true, true, "sitebackend");
				if(returned.size()<1){
					statmentBuild = "INSERT INTO lastplayed(SongTitle, Spot) VALUES( ?, null)";
					runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("listen")){
			statmentBuild = "UPDATE radio SET Listeners = ?";
			dataForSQL.add(String.valueOf(data[0]));
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("dj")){
			statmentBuild = "UPDATE radio SET CurrentDJ = ?";
			dataForSQL.add(String.valueOf(data[0]));
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("title")){
			statmentBuild = "UPDATE radio SET StreamName = ?";
			dataForSQL.add(String.valueOf(data[0]));
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/*public static boolean pushStats(String mod) throws SQLException{
	*	boolean doesExist = false;
	*	boolean result = false;
	*	ArrayList<String> dataForSQL = new ArrayList<String>();
	*	String stamentBuild = "UPDATE ";
	*
	*	return result;
	}*/
	 /*
	 * pushUser is used to access the tables containing the data of all IRC users
	 *
	 * Allows setting of the parting time for away time tracking.
	 * 
	 * Also allows access to the stored settings such as fave alert settings and ignore settings.
	 *
	 *
	 */
	private static ArrayList<String> pushUser(String mod, Object[] data) throws IOException{
		boolean userExists = false;
		ArrayList<String> result = new ArrayList<String>();
		boolean isParted = false;
        ArrayList<Object> returned = new ArrayList<Object>();
		Date date = new Date();
		doesUserExist(data);
        if(mod.equalsIgnoreCase("login")){
            statmentBuild = "SELECT * FROM users WHERE user= ?";
            dataForSQL.clear();
            dataForSQL.add(String.valueOf(data[0]));
            try {
                returned = runCommand(statmentBuild, dataForSQL, true, true, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for(int i=0; i<returned.size(); i++){
                result.add(String.valueOf(returned.get(i)));
            }
        }
        //The logout section saves the current user container(KarrenCon) to the SQL to preserve settings.
        if(mod.equalsIgnoreCase("logout")){
            dataForSQL.clear();

        }
		if(mod.equalsIgnoreCase("back")){
			//Sets botpart to false and sends a message to the server stating how long user has been away
			try{
				statmentBuild = "SELECT botpart FROM users WHERE user= ?";
				dataForSQL.clear();
				dataForSQL.add(String.valueOf(data[0]));
				ArrayList<Object> getIsParted = runCommand(statmentBuild, dataForSQL, true, true, null);
				isParted = (boolean)getIsParted.get(0);
			} catch(SQLException e){
				e.printStackTrace();
				Logging.log(e.getMessage(), true);
			}
			if(isParted){
				try{
					statmentBuild = "UPDATE users SET botpart=false WHERE user= ?";
					dataForSQL.clear();
					dataForSQL.add(String.valueOf(data[0]));
					runCommand(statmentBuild, dataForSQL, false, true, null);
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
				try{
					statmentBuild = "SELECT timepart FROM users WHERE user= ?";
					dataForSQL.clear();
					dataForSQL.add(String.valueOf(data[0]));
					ArrayList<Object> getAwayTime = runCommand(statmentBuild, dataForSQL, true, true, null);
					result.add((String)getAwayTime.get(0));
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
                }
		    } else {
			    try{
			    	statmentBuild = "UPDATE users SET botpart=true, timepart= ? WHERE user= ?";
			    	dataForSQL.add(String.valueOf(date.getTime()));
			    	dataForSQL.add(String.valueOf(data[0]));
			    	runCommand(statmentBuild, dataForSQL, false, true, null);
			    } catch(SQLException e) {
				    e.printStackTrace();
				    Logging.log(e.getMessage(), true);
               }
            }
		}
		return result;
	}
	/*
	 * runCommand is used to compile and run the query generated by the previous methods
	 */
	private static ArrayList<Object> runCommand(String command, ArrayList<String> data, boolean search, boolean pstNeeded, String overrideDB) throws SQLException{
		String activeDb = (String)(bot.getBotConf().getConfigPayload("sqldb"));
		ArrayList<Object> result = new ArrayList<Object>();
		if(overrideDB != null)
			activeDb = overrideDB;
		Connection run = DriverManager.getConnection("jdbc:mysql://" + bot.getBotConf().getConfigPayload("sqlhost") + ":" + bot.getBotConf().getConfigPayload("sqlport") + "/" + activeDb, (String)(bot.getBotConf().getConfigPayload("sqluser")), (String)(bot.getBotConf().getConfigPayload("sqlpass")));
		PreparedStatement pst;
		ResultSet rs = null;
		pst = run.prepareStatement(command);
		if(pstNeeded){
			for(int i=0; i<data.size(); i++){
				pst.setString(i+1, data.get(i));
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
	/*
	 * Returns a date in the format of YYYY-MM-DD to use with the SQL 
	 * 
	 */
	public static String getCurDate(String dateLayout, SimpleDateFormat dateFormat){
		Date date = new Date();
		dateLayout = dateFormat.format(date);
		return dateLayout;
	}
    /*
    doesUserExist - Used to check if the target user exists with in the database,
    if not it creates a new user for the target.

     */
    public static void doesUserExist(Object[] data){
        ArrayList<String> savedUsers = new ArrayList<String>();
        boolean userExists = false;
        try{
            statmentBuild = "SELECT user FROM users";
            ArrayList<Object> usrTemp = runCommand(statmentBuild, dataForSQL, true, false, null);
            for(int i=0; i<usrTemp.size(); i++){
                savedUsers.add((String) usrTemp.get(i));
            }
        } catch(SQLException e) {
            e.printStackTrace();
            Logging.log(e.getMessage(), true);
        }
        if(savedUsers.size() > 0){
            for(String curUser : savedUsers){
                if(String.valueOf(data[0]).equalsIgnoreCase(curUser)){
                    userExists = true;
                }
            }
        }
        if(!userExists){
            //Creates new user in table
            statmentBuild = "INSERT INTO users (user, botpart, timepart, timeWasted) VALUES (?, false, 0, 0)";
            dataForSQL.add(String.valueOf(data[0]));
            try {
                runCommand(statmentBuild, dataForSQL, false, true, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
