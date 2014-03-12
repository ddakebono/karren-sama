/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.io.*;
import java.util.Properties;

public class Karren{
    public static PircBotX bot;
	public static void main(String[] args){
        System.out.println("Beginning startup");
		//Initialize and load our config file
        try {
            initConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Adding the listeners for our commands
		Configuration<PircBotX> config = new Configuration.Builder<>()
			.setName(GlobalVars.botname)
			.setLogin("Karren")
			.setRealName("Karren-sama IRC Bot")
			.setAutoNickChange(true)
			.setNickservPassword(GlobalVars.nickservPass)
			.addListener(new MiscCommands())
            .addListener(new InteractionCommands())
			.addListener(new NewsCommand())
			.addListener(new KillCommand())
			.addListener(new TopicCommand())
			.addListener(new HueCommand())
			.addListener(new HashCommand())
			.setServerHostname(GlobalVars.hostname)
			.addAutoJoinChannel(GlobalVars.channel)
			.buildConfiguration();
		bot = new PircBotX(config);
		
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
        //Initialize the bot
		try{
			bot.startBot();
		} catch (Exception e){
			e.printStackTrace();
			Logging.log(e.toString(), true);
		}
	}
	public static void initConfig() throws IOException{
		Properties cfg = new Properties();
		File check = new File(GlobalVars.botConf);
		if(check.isFile()){
            FlatFileStorage.loadInteractions();
			cfg.load(new FileInputStream(GlobalVars.botConf));
		} else {
            new File("conf").mkdirs();
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
		GlobalVars.djHashGenKey = Integer.parseInt(cfg.getProperty("djHashGenKey", "1"));
        GlobalVars.userCap = Integer.parseInt(cfg.getProperty("maximumUsers", "100"));
		if(!cfg.getProperty("karrenVersion", "0").equalsIgnoreCase(GlobalVars.versionMarker)){
			Logging.log("Updating configuration file!", true);
			mkNewConfig();
		}
	}
	public static void mkNewConfig() throws IOException{
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
		cfg.setProperty("nickservPass", GlobalVars.nickservPass);
		cfg.setProperty("channel", GlobalVars.channel);
		cfg.setProperty("djHashGenKey", String.valueOf(GlobalVars.djHashGenKey));
        cfg.setProperty("maximumUsers", String.valueOf(GlobalVars.userCap));
		cfg.store(new FileOutputStream(GlobalVars.botConf), comment);
		System.out.println("Config file generated! Terminating!");
		Logging.log("Your configuration file has been generated/updated!", false);
		System.exit(0);
	}
}