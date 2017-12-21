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

import org.frostbite.karren.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Karren{

    public static KarrenBot bot;
    public static Logger log;
    public static JsonConfig conf;
    public static final Properties jarProps = new Properties();
    public static String botVersion;
    public static final String confVersion = "1.6";
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

        List<Configuration.ServerEntry> servers = new LinkedList<>();
        servers.add(new Configuration.ServerEntry("irc.chat.twitch.tv", 6667));

        Configuration ircConf = new Configuration.Builder()
                .setAutoReconnect(true)
                .setAutoReconnectAttempts(5)
                .setAutoReconnectDelay(10)
                .setName("karrensamabot")
                .setServerPassword(conf.getDiscordToken())
                .setServers(servers)
                .addListener(new ConnectCommand())
                .addListener(new InteractionCommands())
                .addListener(new KillCommand())
                .addListener(new ShutdownListener())
                .addListener(new StatCommand())
                .addAutoJoinChannels(Arrays.asList(conf.getChannels()))
                .setCapEnabled(true)
                .buildConfiguration();

        bot = new KarrenBot(new PircBotX(ircConf));

        //Start bot
        try {
            bot.initDiscord();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }
}
