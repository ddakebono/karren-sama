package org.frostbite.karren;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

public class Logging {
	public static void log(String out) throws IOException{
		try{
			Writer logfile = new BufferedWriter(new FileWriter("log.log", true));
			if(logfile.equals("")){
				logfile.write("////////Karren-sama bot log file\\\\\\\\");
			}
			Date date = new Date();
			logfile.append("[" + date.toString() + "]" + out + "\n");
			logfile.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
