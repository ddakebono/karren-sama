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
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.IOException;

public class Karren{

    public static KarrenBot bot;
    public static Logger log;
    public static BotConfiguration confOld;
    public static JsonConfig conf;
    public static final String botVersion = "4.0-Alpha";
    public static final String confVersion = "1.0";

	public static void main(String[] args){
        log = LoggerFactory.getLogger(Karren.class);
        //Configs
        confOld = new BotConfiguration();
        try {
            confOld.initConfig(log);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //New config stuff
        conf = new JsonConfig(confVersion);
        conf.loadConfig();
        if(confOld.isDoesExist()){
            conf.importFromOldConf(confOld);
            conf.saveConfig();
            System.exit(0);
        }
        if(conf.checkUpdate(false) || !conf.isSet()){
            Karren.log.info("Please check the configuration file at conf/bot.json and set your required settings.");
            System.exit(0);
        }



        System.setProperty("http.agent", "KarrenSama/" + botVersion);


        //Build our discord client
        IDiscordClient client = null;
        try {
            client = new ClientBuilder().withToken(conf.getDiscordToken()).build();
        } catch (DiscordException e) {
            e.printStackTrace();
        }

        //Setup the objects we need.
        bot = new KarrenBot(client);

        //Pass execution to main bot class.
        bot.initDiscord();
	}
}
