/*
 * Copyright (c) 2017 Owen Bennett.
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
import java.io.InputStream;
import java.util.Properties;

public class Karren{

    public static KarrenBot bot;
    public static Logger log;
    public static JsonConfig conf;
    public static Properties jarProps = new Properties();
    public static String botVersion;
    public static final String confVersion = "1.2";
    public static long startTime = System.currentTimeMillis();

	public static void main(String[] args){
        log = LoggerFactory.getLogger(Karren.class);

        //Load properties from jar
        InputStream propIn = Karren.class.getClassLoader().getResourceAsStream("git.properties");
        try {
            jarProps.load(propIn);
            propIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set some things from the properties
        botVersion = jarProps.getProperty("git.build.version") + "-" + jarProps.getProperty("git.commit.id.abbrev");

        //New config stuff
        conf = new JsonConfig(confVersion);
        conf.loadConfig();
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
