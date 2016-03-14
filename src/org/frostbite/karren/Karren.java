/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
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
