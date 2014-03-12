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
        public static String botConf = "conf/bot.prop";
	//Final storage
		public final static String versionMarker = "v0.5";
    //Interaction check configs
        public static ArrayList<Interactions> interactions = new ArrayList<Interactions>();
	//Active storage
		public static boolean loop = false;
		public static String npSong = "offair";
		public static String iceDJ = "noone";
		public static String iceStreamTitle = "Off-air";
		public static int iceListeners = 0;
		public static int iceMaxListeners = 0;
		public static int hueCount = 0;
		public static ArrayList<String> awayUser = new ArrayList<String>();
		public static int songID = 0;
		public static boolean songChange = false;
		public static int songPlayedAmount = 0;
		public static int songFavCount = 0;
		public static String lpTime = "";
        public static Channel npChannel;
        public static int curUserCount = 0;
        public static KarrenCon[] userList;
        public static String[] userListNicks;
}
