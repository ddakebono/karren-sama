package org.frostbite.karren;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.frostbite.karren.Logging;
import org.pircbotx.hooks.events.MessageEvent;

public class MySQLConnector {
	public static ArrayList<String> sqlPush(String type, String mod, String[] data) throws IOException, SQLException{
		String[] types = {"news", "site", "radio", "stats", "part"};
		ArrayList<String> result = new ArrayList<String>();
		boolean validType = false;
		for(String check : types){
			if(type.equalsIgnoreCase(check)){
				validType = true;
			}
		}
		if(validType){
			if(type.equalsIgnoreCase(types[0])){
				pushNews(mod, data);
			} else
			if(type.equalsIgnoreCase(types[1])){
				pushSite(mod, data);
			} else
			if(type.equalsIgnoreCase(types[2])){
				pushRadio(mod, data);
			} else
			if(type.equalsIgnoreCase(types[3])){
				pushStats(mod);
			} else
			if(type.equalsIgnoreCase(types[4])){
				result.add(pushPart(mod, data));
			}
		}
		return result;
	}
	public static void pushNews(String mod, String[] data) throws IOException{
		ArrayList<String> dataForSQL = new ArrayList<String>();
		String sqldb = "sitebackend";
		String curdate = getSqlDate();
		Logging.log(curdate, true);
		String query = "INSERT INTO news (author, post, id, header, date) VALUES ( ? , ? , null, 1, ?)";
		//adds author info
		dataForSQL.add(data[0]);
		//Adds post
		dataForSQL.add(data[1]);
		//Adds timestamp
		dataForSQL.add(curdate);
		try{
			runCommand(query, dataForSQL, false, true, sqldb);
		} catch(SQLException e) {
			Logging.log(e.toString(), true);
		}
	}
	public static boolean pushSite(String mod, String[] data) throws IOException{
		boolean result = false;
		String statmentBuild = "";
		return false;
	}
	public static boolean pushRadio(String mod, String[] data) throws IOException{
		boolean result = false;
		String statmentBuild = "";
		ArrayList<String> dataForSQL = new ArrayList<String>();
		//Updating now playing song
		if(mod.equalsIgnoreCase("Song")){
			statmentBuild = "UPDATE radio SET NowPlaying= ?";
			dataForSQL.add(data[0]);
			try {
				runCommand(statmentBuild, dataForSQL, false, true, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataForSQL.clear();
			//Moving the last played down and adding new song
			for(int i=GlobalVars.icecastLPNum; i<2; i--){
				statmentBuild = "UPDATE lastplayed dt1, lastplayed dt2 SET dt1.SongTitle = dt2.SongTitle WHERE dt1.Spot = " + i + " AND dt2.Spot = " + (i-1);
				try {
					runCommand(statmentBuild, dataForSQL, false, false, "sitebackend");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			statmentBuild = "UPDATE radio dt1, lastplayed dt2 SET dt2.SongTitle = dt1.NowPlaying WHERE dt2.Spot = '1'";
			try {
				runCommand(statmentBuild, dataForSQL, false, false, "sitebackend");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
		return false;
	}
	public static boolean pushStats(String mod) throws SQLException{
		boolean doesExist = false;
		boolean result = false;
		ArrayList<String> dataForSQL = new ArrayList<String>();
		String stamentBuild = "UPDATE ";
		
		return result;
	}
	/*
	 * pushPart is used to access the tables containing the data of all user afk times
	 * 
	 * Used to track and tell users how long they have been gone.
	 */
	public static String pushPart(String mod, String[] data) throws IOException{
		int index = 0;
		boolean userExists = false;
		String result = null;
		boolean isParted = false;
		ArrayList<String> dataForSQL = new ArrayList<String>();
		Date date = new Date();
		String query;
		PreparedStatement pst;
		ArrayList<String> savedUsers = new ArrayList<String>();
		try{
			query = "SELECT user FROM users";
			ResultSet userGet = runCommand(query, dataForSQL, true, false, "");
			while(userGet.next()){
				savedUsers.add(userGet.getString("user"));
				index++;
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
				query = "SELECT botpart FROM users WHERE user= ?";
				dataForSQL.clear();
				dataForSQL.add(data[0]);
				ResultSet returned1 = runCommand(query, dataForSQL, true, true, "");
				if(returned1.next()){
					isParted = returned1.getBoolean(1);
				} else {
					isParted = false;
				}
			} catch(SQLException e){
				e.printStackTrace();
				Logging.log(e.getMessage(), true);
			}
			if(isParted){
				try{
					query = "UPDATE users SET botpart=false WHERE user= ?";
					dataForSQL.clear();
					dataForSQL.add(data[0]);
					runCommand(query, dataForSQL, false, true, "");
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
				try{
					query = "SELECT timepart FROM users WHERE user= ?";
					dataForSQL.clear();
					dataForSQL.add(data[0]);
					ResultSet returned = runCommand(query, dataForSQL, true, true, "");
					if(returned.next()){
						result = String.valueOf(returned.getObject(1));
					} else {
						result = "0";
					}
				} catch(SQLException e){
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
			}
		} else {
			if(userExists){
				try{
					query = "UPDATE users SET botpart=true, timepart= ? WHERE user= ?";
					dataForSQL.add(String.valueOf(date.getTime()));
					dataForSQL.add(data[0]);
					runCommand(query, dataForSQL, false, true, "");
				} catch(SQLException e) {
					e.printStackTrace();
					Logging.log(e.getMessage(), true);
				}
			} else {
				try{
					query = "INSERT INTO users (user, botpart, timepart) VALUES ( ? , 1 , ? )";
					dataForSQL.add(data[0]);
					dataForSQL.add(String.valueOf(date.getTime()));
					runCommand(query, dataForSQL, false, true, "");
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
	public static ResultSet runCommand(String command, ArrayList<String> data, boolean search, boolean pstNeeded, String overrideDB) throws SQLException{
		String activeDb = GlobalVars.sqldb;
		if(overrideDB != "")
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
		if(search)
			rs = pst.executeQuery();
		if(!search)
			pst.execute();
		return rs;
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
}
