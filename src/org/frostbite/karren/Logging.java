package org.frostbite.karren;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class Logging {
	public static void log(String out, boolean err) throws IOException{
		try{
			Writer logfile = new BufferedWriter(new FileWriter("log.log", true));
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
		Writer logfile = new BufferedWriter(new FileWriter("songs.log", true));
		Date date = new Date();
		logfile.append("[" + date.toString() + "] Now playing updated to \"" + out + "\"\n");
		logfile.close();
	}
}
