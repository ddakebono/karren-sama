/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import java.io.*;
import java.util.Date;

public class Logging {
    private static boolean isInit = false;
    private static void initLogger(){
        new File("logs").mkdirs();
        isInit = true;
    }
	public static void log(String out, boolean err){
        if(!isInit){
            initLogger();
        }
		try{
			Writer logfile = new BufferedWriter(new FileWriter("logs/log.log", true));
			if(logfile.equals("")){
				logfile.write("////////ListenCast Log File\\\\\\\\");
			}
			Date date = new Date();
			if(err){
				logfile.append("[!!! " + date.toString() + " !!!] (ERROR)" + out + "\n");
			} else {
				logfile.append("[" + date.toString() + "]" + out + "\n");
			}
			logfile.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void song(String out) throws IOException{
		Writer logfile = new BufferedWriter(new FileWriter("logs/songs.log", true));
		Date date = new Date();
		logfile.append("[" + date.toString() + "] Now playing updated to \"" + out + "\"\n");
		logfile.close();
	}
}
