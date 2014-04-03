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

public class Karren{
	public static void main(String[] args){
        //Configs
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        Logger log = LoggerFactory.getLogger(Karren.class);
        BotConfiguration botConf = new BotConfiguration();
        try {
            botConf.initConfig(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration<PircBotX> config = new Configuration.Builder<>()
                .setName((String)botConf.getConfigPayload("botname"))
                .setLogin("Karren")
                .setRealName("Karren-sama IRC Bot")
                .setAutoNickChange(true)
                .setNickservPassword((String) botConf.getConfigPayload("nickservpass"))
                .addListener(new MiscCommands())
                .addListener(new InteractionCommands())
                .addListener(new NewsCommand())
                .addListener(new KillCommand())
                .addListener(new TopicCommand())
                .addListener(new HueCommand())
                .setServerHostname((String)botConf.getConfigPayload("hostname"))
                .addAutoJoinChannel((String)botConf.getConfigPayload("channel"))
                .buildConfiguration();
        //Adding the listeners for our commands
		KarrenBot bot = new KarrenBot(config, botConf, log);
		
		//Try and load the JDBC MySQL Driver
		try{
			log.info(bot.getNick() + " version " + bot.getBotConf().getConfigPayload("version") + " is now starting!");
            log.debug("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
            log.debug("Loaded driver!", false);
		} catch(ClassNotFoundException e) {
			log.error("Error While Loading:", e);
		}
        //Initialize the bot
		try{
            log.info(bot.getNick() + " Ready, connecting to " + bot.getBotConf().getConfigPayload("hostname"));
			bot.startBot();
		} catch (Exception e){
            log.error("Error While Loading:", e);
		}
	}
}
