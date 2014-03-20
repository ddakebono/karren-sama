/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import java.util.ArrayList;

import org.pircbotx.Channel;

public class GlobalVars {
    //Interaction check configs
        public static ArrayList<Interactions> interactions = new ArrayList<Interactions>();
	//Active storage
		public static boolean loop = false;
		public static String npSong = "offair";
		public static String iceDJ = "noone";
		public static String iceStreamTitle = "Off-air";
		public static int iceListeners = 0;
		public static int iceMaxListeners = 0;
		public static int songID = 0;
		public static boolean songChange = false;
		public static int songPlayedAmount = 0;
		public static int songFavCount = 0;
		public static String lpTime = "";
        public static Channel npChannel;
}
