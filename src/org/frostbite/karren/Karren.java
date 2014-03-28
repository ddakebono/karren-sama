/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.frostbite.karren.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.io.IOException;

public class Karren{
	public static void main(String[] args){
        System.out.println("Beginning startup");
        //Configs
        BotConfiguration botConf = new BotConfiguration();
        try {
            botConf.initConfig();
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
		KarrenBot bot = new KarrenBot(config, botConf);
		
		//Try and load the JDBC MySQL Driver
		try{
			Logging.log(bot.getNick() + " version " + bot.getBotConf().getConfigPayload("version") + " is now starting!", false);
			Logging.log("Trying to load MySQL Driver...", false);
			Class.forName("com.mysql.jdbc.Driver");
			Logging.log("Loaded driver!", false);
		} catch(ClassNotFoundException e) {
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
}
