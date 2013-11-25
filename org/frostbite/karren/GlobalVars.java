package org.frostbite.karren;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GlobalVars {
	public static boolean loop = false;
	public static String versionMarker = "v0.1";
	public static String botname = "";
	public static String hostname = "";
	public static String sqlhost = "";
	public static String sqlport = "";
	public static String sqluser = "";
	public static String sqldb = "";
	public static String sqlpass = "";
	public static String icelink = "";
	//Active storage
	public static String npSong = "airoff";
	public static String npSongNew = "offair";
	public static int hueCount = 0;
	public static String icelogFile = "/srv/ircd/irc-bots/icelog.log";
	public static ArrayList<String> awayUser = new ArrayList<String>();
	//Config loader
}
