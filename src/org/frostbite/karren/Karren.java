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
import org.pircbotx.Configuration.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Karren {
	public static void main(String[] args) throws Exception{
		//Initialize and load our config file
		initConfig();
		//Adding the listeners for our commands
		@SuppressWarnings("unchecked")
		Configuration config = new Configuration.Builder()
			.setName(GlobalVars.botname)
			.setLogin("Karren")
			.setAutoNickChange(true)
			.addListener(new NPCommand())
			.addListener(new EchoCommand())
			.addListener(new NewsCommand())
			.addListener(new KillCommand())
			.addListener(new TerminateBot())
			.addListener(new TopicCommand())
			.addListener(new HueCommand())
			.addListener(new GayCommand())
			.addListener(new OriginCommand())
			.addListener(new HelpCommand())
			.addListener(new BradCommand())
			.addListener(new TalkToCommand())
			.addListener(new VersionCommand())
			.setServerHostname(GlobalVars.hostname)
			.addAutoJoinChannel(GlobalVars.channel)
			.buildConfiguration();
		PircBotX bot = new PircBotX(config);
		
		//Try and load the JDBC MySQL Driver
		try{
			Logging.log(GlobalVars.botname + " version " + GlobalVars.versionMarker + " is now starting!");
			Logging.log("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
			Logging.log("Loaded driver!");
		} catch(ClassNotFoundException e) {
			Logging.log(e.toString());
		}
		try{
			bot.startBot();
		} catch (Exception e){
			e.printStackTrace();
			Logging.log(e.toString());
		}
		bot.sendRaw().rawLine("PRIVMSG nickserv :identify " + GlobalVars.nickservPass);
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
			GlobalVars.icelink = cfg.getProperty("icelink", "changeme");
			GlobalVars.nickservPass = cfg.getProperty("nickservPass", "changeme");
			GlobalVars.channel = cfg.getProperty("channel", "#changeme");
		} else {
			cfg.setProperty("botname", "Karren-sama");
			cfg.setProperty("hostname", "0.0.0.0");
			cfg.setProperty("sqlhost", "0.0.0.0");
			cfg.setProperty("sqlport", "3306");
			cfg.setProperty("sqluser", "changeme");
			cfg.setProperty("sqlpass", "changeme");
			cfg.setProperty("sqldb", "changeme");
			cfg.setProperty("icelink", "changeme");
			cfg.setProperty("nickservPass", "changeme");
			cfg.setProperty("channel", "changeme");
			cfg.store(new FileOutputStream("bot.prop"), comment);
			System.out.println("Config file generated! Terminating!");
			Logging.log("Your configuration file has been generated!");
			System.exit(0);
		}
	}
}
