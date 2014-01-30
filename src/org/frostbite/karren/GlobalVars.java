/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import java.util.ArrayList;

import org.pircbotx.Channel;

public class GlobalVars {
	//Config Storage
		//SQL Configs
			public static String sqlhost = "";
			public static String sqlport = "";
			public static String sqluser = "";
			public static String sqldb = "";
			public static String sqlpass = "";
		//Icecast Configs
			public static String icecastAdminUsername = "";
			public static String icecastAdminPass = "";
			public static String icecastHost = "";
			public static String icecastPort = "";
			public static String icecastMount = "";
			public static int djHashGenKey = 0;
		//IRC Configs
			public static String nickservPass = "";
			public static String channel = "";
			public static String botname = "";
			public static String hostname = "";
            public static int userCap = 0;
	//Final storage
		public final static String versionMarker = "v0.5";
	//Active storage
		public static boolean loop = false;
		public static String npSong = "offair";
		public static String npSongNew = "offair";
		public static String iceDJ = "noone";
		public static String iceStreamTitle = "Off-air";
		public static int iceListeners = 0;
		public static int iceMaxListeners = 0;
		public static int hueCount = 0;
		public static ArrayList<String> awayUser = new ArrayList<String>();
		public static int songID = 0;
		public static boolean songChange = false;
		public static String songLastPlayed = "";
		public static int songPlayedAmount = 0;
		public static int songFavCount = 0;
        public static ArrayList<String> nicks = new ArrayList<String>();
		public static ArrayList<KarrenCon> userList = new ArrayList<KarrenCon>();
        public static KarrenCon[] userListNew;
        public static String[] userListNicks;
		public static String lpTime = "";
        public static Channel npChannel;
}
