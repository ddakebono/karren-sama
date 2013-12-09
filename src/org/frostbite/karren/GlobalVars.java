package org.frostbite.karren;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GlobalVars {
	//Config Storage
	public static String botname = "";
	public static String hostname = "";
	public static String sqlhost = "";
	public static String sqlport = "";
	public static String sqluser = "";
	public static String sqldb = "";
	public static String sqlpass = "";
	//Icecast will be changed soon
	public static String icelink = "";
	public static String nickservPass = "";
	public static String channel = "";
	//Final storage
	public final static String versionMarker = "v0.1.1";
	//Active storage
	public static boolean loop = false;
	public static String npSong = "airoff";
	public static String npSongNew = "offair";
	public static int hueCount = 0;
	public static ArrayList<String> awayUser = new ArrayList<String>();
}
