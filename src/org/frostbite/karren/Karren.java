package org.frostbite.karren;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.BradCommand;
import org.frostbite.karren.listeners.EchoCommand;
import org.frostbite.karren.listeners.GayCommand;
import org.frostbite.karren.listeners.HelpCommand;
import org.frostbite.karren.listeners.HueCommand;
import org.frostbite.karren.listeners.KillCommand;
import org.frostbite.karren.listeners.NPCommand;
import org.frostbite.karren.listeners.NewsCommand;
import org.frostbite.karren.listeners.OriginCommand;
import org.frostbite.karren.listeners.SiteCommand;
import org.frostbite.karren.listeners.TalkToCommand;
import org.frostbite.karren.listeners.TopicCommand;
import org.frostbite.karren.listeners.VersionCommand;
import org.frostbite.karren.Logging;
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
			.setRealName("Karren-sama IRC Bot")
			.setAutoNickChange(true)
			.setNickservPassword(GlobalVars.nickservPass)
			.addListener(new NPCommand())
			.addListener(new EchoCommand())
			.addListener(new NewsCommand())
			.addListener(new KillCommand())
			.addListener(new TopicCommand())
			.addListener(new HueCommand())
			.addListener(new GayCommand())
			.addListener(new OriginCommand())
			.addListener(new HelpCommand())
			.addListener(new BradCommand())
			.addListener(new TalkToCommand())
			.addListener(new VersionCommand())
			.addListener(new SiteCommand()) //Doesn't function currently.
			.setServerHostname(GlobalVars.hostname)
			.addAutoJoinChannel(GlobalVars.channel)
			.buildConfiguration();
		PircBotX bot = new PircBotX(config);
		
		//Try and load the JDBC MySQL Driver
		try{
			Logging.log(GlobalVars.botname + " version " + GlobalVars.versionMarker + " is now starting!", false);
			Logging.log("Trying to load MySQL Driver...", false);
			Class.forName("com.mysql.jdbc.Driver");
			Logging.log("Loaded driver!", false);
		} catch(ClassNotFoundException e) {
			Logging.log(e.toString(), true);
		}
		//Start up the ListenCast thread
		try{
			ListenCast lc = new ListenCast(bot);
			lc.start();
		} catch(Exception e){
			e.printStackTrace();
			Logging.log(e.toString(), true);
		}
		try{
			bot.startBot();
		} catch (Exception e){
			e.printStackTrace();
			Logging.log(e.toString(), true);
		}
	}
	public static void initConfig() throws IOException{
		Properties cfg = new Properties();
		File check = new File("bot.prop");
		if(check.isFile()){
			cfg.load(new FileInputStream("bot.prop"));
		}
		GlobalVars.botname = cfg.getProperty("botname", "Karren-sama");
		GlobalVars.hostname = cfg.getProperty("hostname", "0.0.0.0");
		GlobalVars.sqlhost = cfg.getProperty("sqlhost", "0.0.0.0");
		GlobalVars.sqlport = cfg.getProperty("sqlport", "3306");
		GlobalVars.sqluser = cfg.getProperty("sqluser", "changeme");
		GlobalVars.sqlpass = cfg.getProperty("sqlpass", "changeme");
		GlobalVars.sqldb = cfg.getProperty("sqldb", "changeme");
		GlobalVars.icecastAdminUsername = cfg.getProperty("icecastAdminUsername", "changeme"); 
		GlobalVars.icecastAdminPass = cfg.getProperty("icecastAdminPass", "changeme");
		GlobalVars.icecastHost = cfg.getProperty("icecastHost", "0.0.0.0");
		GlobalVars.icecastPort = cfg.getProperty("icecastPort", "8000");
		GlobalVars.icecastMount = cfg.getProperty("icecastMount", "changeme.mp3");
		GlobalVars.nickservPass = cfg.getProperty("nickservPass", "changeme");
		GlobalVars.channel = cfg.getProperty("channel", "#changeme");
		GlobalVars.icecastLPNum = Integer.parseInt(cfg.getProperty("icecastLastPlayedSlots", "5"));
		if(!cfg.getProperty("karrenVersion", "0").equalsIgnoreCase(GlobalVars.versionMarker)){
			Logging.log("Updating configuration file!", true);
			mkNewConfig();
		}
	}
	public static void mkNewConfig() throws FileNotFoundException, IOException{
		Properties cfg = new Properties();
		String comment = "Karren-sama IRC bot properties file.";
		cfg.setProperty("karrenVersion", GlobalVars.versionMarker);
		cfg.setProperty("botname", GlobalVars.botname);
		cfg.setProperty("hostname", GlobalVars.hostname);
		cfg.setProperty("sqlhost", GlobalVars.sqlhost);
		cfg.setProperty("sqlport", GlobalVars.sqlport);
		cfg.setProperty("sqluser", GlobalVars.sqluser);
		cfg.setProperty("sqlpass", GlobalVars.sqlpass);
		cfg.setProperty("sqldb", GlobalVars.sqldb);
		cfg.setProperty("icecastAdminUsername", GlobalVars.icecastAdminUsername);
		cfg.setProperty("icecastAdminPass", GlobalVars.icecastAdminPass);
		cfg.setProperty("icecastHost", GlobalVars.icecastHost);
		cfg.setProperty("icecastPort", GlobalVars.icecastPort);
		cfg.setProperty("icecastMount", GlobalVars.icecastMount);
		cfg.setProperty("icecastLastPlayedSlots", String.valueOf(GlobalVars.icecastLPNum));
		cfg.setProperty("nickservPass", GlobalVars.nickservPass);
		cfg.setProperty("channel", GlobalVars.channel);
		cfg.store(new FileOutputStream("bot.prop"), comment);
		System.out.println("Config file generated! Terminating!");
		Logging.log("Your configuration file has been generated/updated!", false);
		System.exit(0);
	}
}
