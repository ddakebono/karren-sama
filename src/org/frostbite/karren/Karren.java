/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Karren{

    public static KarrenBot bot;
    public static Logger log;
    public static JsonConfig conf;
    public static final Properties jarProps = new Properties();
    public static String botVersion;
    public static final String confVersion = "1.7";
    public static final long startTime = System.currentTimeMillis();

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
        log.info("Karren-sama version " + botVersion + " starting up!");

        //New config stuff
        conf = new JsonConfig(confVersion);
        conf.loadConfig();
        if(conf.checkUpdate(false) || !conf.isSet()){
            Karren.log.info("Please check the configuration file at conf/bot.json and set your required settings.");
            System.exit(0);
        }

        System.setProperty("http.agent", "KarrenSama/" + botVersion);

        //Build our discord client
        JDABuilder client;

        //client = new ClientBuilder().withToken(Karren.conf.getDiscordToken()).withRecommendedShardCount().build();
        client = JDABuilder.createDefault(conf.getDiscordToken());

        //Setup the objects we need.
        Karren.bot = new KarrenBot(client);

        //Pass execution to main bot class.
        Karren.bot.initDiscord();
	}
}
