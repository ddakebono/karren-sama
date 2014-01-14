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
	public static ArrayList<String> sqlPush(String type, String mod, String[] data) throws IOException, SQLException{
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
			case "part":
				result.add(pushPart(mod, data));
				break;
			case "hash":
				result.add(pushHash(mod, data));
				break;
			case "song":
				result.addAll(pushSong(mod, data));
				break;
			case "fave":
				result.addAll(getFave());
			default:
				result.add(null);
				break;
		}
		return result;
	}
	private static ArrayList<String> pushSong(String mod ,String[] data) throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Object> returned;
		ArrayList<String> dataForSQL = new ArrayList<String>();
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
				dataForSQL.add(getCurTime());
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
				dataForSQL.add(getCurTime());
				runCommand(statmentBuild, dataForSQL, false, true, null);
				GlobalVars.songChange = false;
			}
			Logging.song(GlobalVars.npSong + ":" + GlobalVars.songID + ":" + GlobalVars.songPlayedAmount);
		}
		if(mod.equalsIgnoreCase("fave")){
			statmentBuild = "SELECT * FROM UserFaves WHERE SongID = ? AND User = ?";
			dataForSQL.add(data[0]);
			dataForSQL.add(data[1]);
			returned = runCommand(statmentBuild, dataForSQL, true, true, null);
			dataForSQL.clear();
			if(returned.size()<1){
				statmentBuild = "INSERT INTO UserFaves(ID, User, SongID) VALUES (null, ?, ?)";
				dataForSQL.add(data[1]);
				dataForSQL.add(data[0]);
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
		ArrayList<String> dataForSQL = new ArrayList<String>();
		statmentBuild = "SELECT User FROM UserFaves WHERE SongID= ?";
		dataForSQL.add(String.valueOf(GlobalVars.songID));
		returned = runCommand(statmentBuild, dataForSQL, true, true, null);
		for(Object nick : returned){
			result.add((String)nick);
		}
		return result;
	}
	private static void pushNews(String mod, String[] data) throws IOException{
		ArrayList<String> dataForSQL = new ArrayList<String>();
		String curdate = getSqlDate();
		Logging.log(curdate, true);
		statmentBuild = "INSERT INTO news (author, post, id, header, date) VALUES ( ? , ? , null, 1, ?)";
		//adds author info
		dataForSQL.add(data[0]);
		//Adds post
		dataForSQL.add(data[1]);
		//Adds timestamp
		dataForSQL.add(curdate);
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
	private static String pushHash(String mod, String[] data) throws SQLException{
		ArrayList<String> dataForSQL = new ArrayList<String>();
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
			for(int i=0; i<data[0].length(); i++){
				hashTemp = hashTemp * data[0].charAt(i);
			}
			hashTemp = hashTemp*GlobalVars.djHashGenKey;
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
			dataForSQL.add(data[0]);
			dataForSQL.add(String.valueOf(resultHash));
			runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			dataForSQL.clear();
		
		} else {
			statmentBuild = "SELECT DJHash FROM `Radio-DJ` WHERE DJName= ?";
			dataForSQL.add(data[0]);
			result = runCommand(statmentBuild, dataForSQL, true, true, "sitebackend");
			resultHash = String.valueOf(result.get(0));
		}
		return resultHash;
	}
	private static String pushRadio(String mod, String[] data) throws IOException{
		String result = "";
		ArrayList<String> dataForSQL = new ArrayList<String>();
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
			dataForSQL.add(data[0]);
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("dj")){
			statmentBuild = "UPDATE radio SET CurrentDJ = ?";
			dataForSQL.add(data[0]);
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mod.equalsIgnoreCase("title")){
			statmentBuild = "UPDATE radio SET StreamName = ?";
			dataForSQL.add(data[0]);
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
	 * pushPart is used to access the tables containing the data of all user afk times
	 * 
	 * Used to track and tell users how long they have been gone.
	 */
	private static String pushPart(String mod, String[] data) throws IOException{
		boolean userExists = false;
		String result = null;
		boolean isParted = false;
		ArrayList<String> dataForSQL = new ArrayList<String>();
		Date date = new Date();
		ArrayList<String> savedUsers = new ArrayList<String>();
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
				if(data[0].equalsIgnoreCase(curUser)){
					userExists = true;
				}
			}
		}
		if(mod.equalsIgnoreCase("back")){
			//Sets botpart to false and sends a message to the server stating how long user has been away
			try{
				statmentBuild = "SELECT botpart FROM users WHERE user= ?";
				dataForSQL.clear();
				dataForSQL.add(data[0]);
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
					dataForSQL.add(data[0]);
					runCommand(statmentBuild, dataForSQL, false, true, null);
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
				try{
					statmentBuild = "SELECT timepart FROM users WHERE user= ?";
					dataForSQL.clear();
					dataForSQL.add(data[0]);
					ArrayList<Object> getAwayTime = runCommand(statmentBuild, dataForSQL, true, true, null);
					result = (String)getAwayTime.get(0);
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
			}
		} else {
			if(userExists){
				try{
					statmentBuild = "UPDATE users SET botpart=true, timepart= ? WHERE user= ?";
					dataForSQL.add(String.valueOf(date.getTime()));
					dataForSQL.add(data[0]);
					runCommand(statmentBuild, dataForSQL, false, true, null);
				} catch(SQLException e) {
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
			} else {
				try{
					statmentBuild = "INSERT INTO users (user, botpart, timepart) VALUES ( ? , 1 , ? )";
					dataForSQL.add(data[0]);
					dataForSQL.add(String.valueOf(date.getTime()));
					runCommand(statmentBuild, dataForSQL, false, true, null);
				} catch(SQLException e) {
					e.printStackTrace();
					Logging.log(e.toString(), true);
				}
			}
		}
		return result;
	}
	/*
	 * runCommand is used to compile and run the query generated by the previous methods
	 */
	private static ArrayList<Object> runCommand(String command, ArrayList<String> data, boolean search, boolean pstNeeded, String overrideDB) throws SQLException{
		String activeDb = GlobalVars.sqldb;
		ArrayList<Object> result = new ArrayList<Object>();
		if(overrideDB != null)
			activeDb = overrideDB;
		Connection run = DriverManager.getConnection("jdbc:mysql://" + GlobalVars.sqlhost + ":" + GlobalVars.sqlport + "/" + activeDb, GlobalVars.sqluser, GlobalVars.sqlpass);
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
	public static String getSqlDate(){
		String sqldate = "0000-00-00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		sqldate = dateFormat.format(date);
		return sqldate;
	}
	public static String getCurTime(){
		String curTime = "00-00-0000 00:00:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy @ HH:mm:ss");
		Date date = new Date();
		curTime = dateFormat.format(date);
		return curTime;
	}
}
