/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

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
                .addListener(new KillCommand())
                .addListener(new TopicCommand())
                .addListener(new HueCommand())
                .addListener(new HelpCommand())
                .addListener(new ConnectListener())
                .addListener(new DisconnectListener())
                .addListener(new SCRAMCommand())
                .addListener(new ServerPingListener())
                .setEncoding(Charset.forName("UTF-8"))
                .setServerHostname(botConf.getHostname())
                .setAutoReconnect(true)
                .buildConfiguration();
        //Setup the objects we need.
		KarrenBot bot = new KarrenBot(config, botConf, log, out);
        BotWatchdog watchdog = new BotWatchdog(log, bot);
		ConsoleInputThread con = new ConsoleInputThread(watchdog);
		//Try and load the JDBC MySQL Driver
		try{
			log.info(bot.getNick() + " version " + botConf.getVersionMarker() + " is now starting!");
            log.debug("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
            log.debug("Loaded driver!");
		} catch(ClassNotFoundException e) {
			log.error("Error While Loading:", e);
		}
        //Fire up the watchdog, bot and the console command stuff.
        con.start();
        watchdog.start();
	}
}
