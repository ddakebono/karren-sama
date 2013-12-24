package org.frostbite.karren.listencast;

import java.io.IOException;
import java.sql.SQLException;

import org.frostbite.karren.MySQLConnector;

public class SongDatabase {
	/*
	 * onSongChange checks for the song in the database and if it finds one
	 * loads the data into the active storage for use in the .np listing.
	 * 
	 */
	public static void onSongChange(String song) throws IOException, SQLException{
		String[] data = new String[1];
		data[0] = song;
		MySQLConnector.sqlPush("song", "", data);
	}
}
