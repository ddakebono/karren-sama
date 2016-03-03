/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;

import java.io.IOException;

public class Karren{

    public static KarrenBot bot;
    public static Logger log;
    public static BotConfiguration conf;

	public static void main(String[] args){
        log = LoggerFactory.getLogger(Karren.class);
        //Configs
        conf = new BotConfiguration();
        try {
            conf.initConfig(log);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build our discord client
        IDiscordClient client = null;
        try {
            client = new ClientBuilder().withLogin(conf.getEmailAddress(), conf.getDiscordPass()).build();
        } catch (DiscordException e) {
            e.printStackTrace();
        }

        //Setup the objects we need.
        bot = new KarrenBot(client);

		//Try and load the JDBC MySQL Driver
		try{
			log.info("Karren-sama version " + conf.getVersionMarker() + " is now starting!");
            log.debug("Trying to load MySQL Driver...");
			Class.forName("com.mysql.jdbc.Driver");
            log.debug("Loaded driver!");
		} catch(ClassNotFoundException e) {
			log.error("Error While Loading:", e);
		}
        //Fire up the watchdog, bot and the console command stuff.
        bot.initDiscord();
	}
}
