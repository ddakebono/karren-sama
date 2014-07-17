/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.frostbite.karren.OsSpecific.OsDiscovery;
import org.frostbite.karren.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

public class Karren{
	public static void main(String[] args){
        OutputWindow out = null;
        if(args.length==0 || !args[0].equalsIgnoreCase("nogui")) {
            out = new OutputWindow("Karren-sama IRC bot");
            out.displayWindow();
        }
        Logger log = LoggerFactory.getLogger(Karren.class);
        boolean enableServiceControl = false;
        int osType = 0; //0 = Unknown, 1 = Windows, 2 = Linux
        //Windows specific check, bot will be elevated to allow access to services controls
        OsDiscovery check = new OsDiscovery();
        switch(check.getSystemType()){
            case "Windows":
                osType = 1;
                log.debug("Windows operating system detected.");
                if(!check.checkIfElevated())
                    log.error("To make full use of Windows specific features like Service control the bot must be started with Administrative permissions.");
                else
                    enableServiceControl = true;
                break;
            case "Linux":
                log.debug("Linux based operating system detected.");
                log.info("Currently only Init.d is supported in the bot.");
                osType = 2;
                try {
                    if(!check.checkIfInitd()){
                        log.error("Either your system configuration has an issue or your system doesn't use Init.d. Service control disabled!");
                    } else {
                        enableServiceControl = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //Configs
        BotConfiguration botConf = new BotConfiguration();
        try {
            botConf.initConfig(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration<PircBotX> config = new Configuration.Builder<>()
                .setName(botConf.getBotname())
                .setLogin("Karren")
                .setRealName("Karren-sama IRC Bot")
                .setAutoNickChange(true)
                .setNickservPassword(botConf.getNickservPass())
                .setServerPassword(botConf.getServerPassword())
                .addListener(new MiscCommands())
                .addListener(new InteractionCommands())
                .addListener(new NewsCommand())
                .addListener(new KillCommand())
                .addListener(new TopicCommand())
                .addListener(new HueCommand())
                .addListener(new HelpCommand())
                .addListener(new SpaceCommands())
                .addListener(new ConnectListener())
                .addListener(new DisconnectListener())
                .addListener(new SystemServiceCommands())
                .setEncoding(Charset.forName("UTF-8"))
                .setServerHostname(botConf.getHostname())
                .setAutoReconnect(true)
                .buildConfiguration();
        //Adding the listeners for our commands
		KarrenBot bot = new KarrenBot(config, botConf, log, osType, out, enableServiceControl);
		ConsoleInputThread con = new ConsoleInputThread(bot);
        con.start();
		//Try and load the JDBC MySQL Driver
		try{
			log.info(bot.getNick() + " version " + botConf.getVersionMarker() + " is now starting!");
            log.debug("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
            log.debug("Loaded driver!");
		} catch(ClassNotFoundException e) {
			log.error("Error While Loading:", e);
		}
        //Initialize the bot
		try{
            log.info(bot.getNick() + " Ready, connecting to " + botConf.getHostname());
            if(botConf.getConnectToIRC().equalsIgnoreCase("true"))
                bot.startBot();
		} catch (Exception e){
            log.error("Error While Loading:", e);
		}
	}
}
