package org.frostbite.karren;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.frostbite.karren.listeners.BradCommand;
import org.frostbite.karren.listeners.EchoCommand;
import org.frostbite.karren.listeners.GayCommand;
import org.frostbite.karren.listeners.HelpCommand;
import org.frostbite.karren.listeners.HueCommand;
import org.frostbite.karren.listeners.KillCommand;
import org.frostbite.karren.listeners.NPCommand;
import org.frostbite.karren.listeners.NewsCommand;
import org.frostbite.karren.listeners.OriginCommand;
import org.frostbite.karren.listeners.RandCommand;
import org.frostbite.karren.listeners.TalkToCommand;
import org.frostbite.karren.listeners.TerminateBot;
import org.frostbite.karren.listeners.TopicCommand;
import org.frostbite.karren.listeners.VersionCommand;
import org.pircbotx.PircBotX;

public class Karren {
	public static void main(String[] args) throws Exception{
		PircBotX bot = new PircBotX();
		//Initialize and load our config file
		initConfig();
		//Adding the listeners for our commands
		bot.getListenerManager().addListener(new NPCommand());
		bot.getListenerManager().addListener(new EchoCommand());
		bot.getListenerManager().addListener(new NewsCommand());
		bot.getListenerManager().addListener(new RandCommand());
		bot.getListenerManager().addListener(new KillCommand());
		bot.getListenerManager().addListener(new TerminateBot());
		bot.getListenerManager().addListener(new TopicCommand());
		bot.getListenerManager().addListener(new HueCommand());
		bot.getListenerManager().addListener(new GayCommand());
		bot.getListenerManager().addListener(new OriginCommand());
		bot.getListenerManager().addListener(new HelpCommand());
		bot.getListenerManager().addListener(new BradCommand());
		bot.getListenerManager().addListener(new TalkToCommand());
		bot.getListenerManager().addListener(new VersionCommand());
		//Try and load the JDBC MySQL Driver
		try{
			Logging.log(GlobalVars.botname + " version " + GlobalVars.versionMarker + " is now starting!");
			Logging.log("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
			Logging.log("Loaded driver!");
		} catch(ClassNotFoundException e) {
			Logging.log(e.toString());
		}
		bot.setName(GlobalVars.botname);
		bot.setLogin("Karren");
		bot.connect(GlobalVars.hostname);
		bot.setVerbose(true);
		bot.joinChannel("#minecraft");
		bot.sendMessage("nickserv", "identify CRaZyPANTS_Servers");
	}
	public static void initConfig() throws IOException{
		Properties cfg = new Properties();
		String comment = "Karren-sama IRC bot properties file.";
		File check = new File("bot.prop");
		if(check.isFile()){
			cfg.load(new FileInputStream("bot.prop"));
			GlobalVars.botname = cfg.getProperty("botname", "Karren-sama");
			GlobalVars.hostname = cfg.getProperty("hostname", "0.0.0.0");
			GlobalVars.sqlhost = cfg.getProperty("sqlhost", "0.0.0.0");
			GlobalVars.sqlport = cfg.getProperty("sqlport", "3306");
			GlobalVars.sqluser = cfg.getProperty("sqluser", "changeme");
			GlobalVars.sqlpass = cfg.getProperty("sqlpass", "changeme");
			GlobalVars.sqldb = cfg.getProperty("sqldb", "changeme");
		} else {
			cfg.setProperty("botname", "Karren-sama");
			cfg.setProperty("hostname", "0.0.0.0");
			cfg.setProperty("sqlhost", "0.0.0.0");
			cfg.setProperty("sqlport", "3306");
			cfg.setProperty("sqluser", "changeme");
			cfg.setProperty("sqlpass", "changeme");
			cfg.setProperty("sqldb", "changeme");
			cfg.store(new FileOutputStream("bot.prop"), comment);
		}
	}
}
