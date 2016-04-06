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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BotConfiguration {
    private String sqlhost;
    private String sqlport;
    private String sqluser;
    private String sqldb;
    private String sqlpass;
    private String icecastAdminUsername;
    private String icecastAdminPass;
    private String icecastHost;
    private String icecastPort;
    private String icecastMount;
    private String streamAnnounceChannel;
    private String discordToken;
    private String guildId;
    private String allowSQLRW;
    private String enableListencast;
    private String listencastAnnounce;
    private String enableInteractions;
    private String commandPrefix;
    private String connectToDiscord;
    private String extPort;
    private String extAddr;
    private final String versionMarker = "2.1-DISCORD";
    /*
    Config Getters
     */

    public String getDiscordToken() {
        return discordToken;
    }
    public String getGuildId() {
        return guildId;
    }
    public String getListencastAnnounce() {
        return listencastAnnounce;
    }
    public String getSqlhost() {
        return sqlhost;
    }
    public String getSqlport() {
        return sqlport;
    }
    public String getSqluser() {
        return sqluser;
    }
    public String getSqldb() {
        return sqldb;
    }
    public String getVersionMarker() {
        return versionMarker;
    }
    public String getStreamAnnounceChannel() {
        return streamAnnounceChannel;
    }
    public String getConnectToDiscord() {return connectToDiscord;}
    public String getIcecastMount() {
        return icecastMount;
    }
    public String getIcecastPort() {
        return icecastPort;
    }
    public String getIcecastHost() {
        return icecastHost;
    }
    public String getIcecastAdminPass() {
        return icecastAdminPass;
    }
    public String getIcecastAdminUsername() {
        return icecastAdminUsername;
    }
    public String getSqlpass() {
        return sqlpass;
    }
    public String getAllowSQLRW() {
        return allowSQLRW;
    }
    public String getEnableListencast() {
        return enableListencast;
    }
    public String getEnableInteractions() {
        return enableInteractions;
    }
    public String getCommandPrefix() { return commandPrefix; }
    public String getExtPort() {
        return extPort;
    }
    public String getExtAddr() {
        return extAddr;
    }

    /*
        Config Loader.
        */
    public void initConfig(Logger log) throws IOException {
        String[] testMount;
        Properties cfg = new Properties();
        File check = new File("conf/bot.prop");
        if(check.isFile()){
            cfg.load(new FileInputStream("conf/bot.prop"));
        } else {
            boolean conf = new File("conf").mkdirs();
            if(!conf && !new File("conf").isDirectory()) {
                log.error("CONFIGURATION FOLDER COULD NOT BE CREATED!");
                log.error("Shutting down bot due to error.");
                System.exit(1);
            }
        }
        sqlhost = cfg.getProperty("sqlhost", "0.0.0.0");
        sqlport = cfg.getProperty("sqlport", "3306");
        sqluser = cfg.getProperty("sqluser", "changeme");
        sqlpass = cfg.getProperty("sqlpass", "changeme");
        sqldb = cfg.getProperty("sqldb", "changeme");
        icecastAdminUsername = cfg.getProperty("icecastAdminUsername", "changeme");
        icecastAdminPass = cfg.getProperty("icecastAdminPass", "changeme");
        icecastHost = cfg.getProperty("icecastHost", "0.0.0.0");
        icecastPort = cfg.getProperty("icecastPort", "8000");
        icecastMount = cfg.getProperty("icecastMount", "changeme.ogg");
        streamAnnounceChannel = cfg.getProperty("streamAnnounceChannel", "0");
        allowSQLRW = cfg.getProperty("allowSQLReadWrite", "true");
        enableInteractions = cfg.getProperty("enableInteractionSystem", "true");
        enableListencast = cfg.getProperty("enableListencastSystem", "true");
        commandPrefix = cfg.getProperty("commandPrefix", ".");
        connectToDiscord = cfg.getProperty("connectToDiscord", "true");
        discordToken = cfg.getProperty("BotAccountToken", "hackme");
        extAddr = cfg.getProperty("ExtenderListenAddress", "127.0.0.1");
        extPort = cfg.getProperty("ExtenderListenPort", "8281");
        guildId = cfg.getProperty("GuildID", "0");
        listencastAnnounce = cfg.getProperty("ListencastAnnounce", "true");
        if(!cfg.getProperty("karrenVersion", "0").equalsIgnoreCase(versionMarker)){
            log.warn("Updating configuration file!");
            mkNewConfig(log);
        }
        testMount = icecastMount.split("\\.");
        if(testMount.length>2)
            log.error("icecastMount should only have one . in it. (EX. stream.ogg)");
        else if(testMount[1].equalsIgnoreCase("mp3"))
            log.info("If you're using mp3 encoding on your stream you may have issues with Unicode characters in song metadata.");

    }
    public void mkNewConfig(Logger log) throws IOException {
        Properties cfg = new Properties();
        String comment = "Karren-sama IRC bot properties file.";
        cfg.setProperty("karrenVersion", versionMarker);
        cfg.setProperty("sqlhost", sqlhost);
        cfg.setProperty("sqlport", sqlport);
        cfg.setProperty("sqluser", sqluser);
        cfg.setProperty("sqlpass", sqlpass);
        cfg.setProperty("sqldb", sqldb);
        cfg.setProperty("icecastAdminUsername", icecastAdminUsername);
        cfg.setProperty("icecastAdminPass", icecastAdminPass);
        cfg.setProperty("icecastHost", icecastHost);
        cfg.setProperty("icecastPort", icecastPort);
        cfg.setProperty("icecastMount", icecastMount);
        cfg.setProperty("streamAnnounceChannel", streamAnnounceChannel);
        cfg.setProperty("allowSQLReadWrite", allowSQLRW);
        cfg.setProperty("enableInteractionSystem", enableInteractions);
        cfg.setProperty("enableListencastSystem", enableListencast);
        cfg.setProperty("commandPrefix", commandPrefix);
        cfg.setProperty("connectToDiscord", connectToDiscord);
        cfg.setProperty("BotAccountToken", discordToken);
        cfg.setProperty("ExtenderListenPort", extPort);
        cfg.setProperty("ExtenderListenAddress", extAddr);
        cfg.setProperty("GuildID", guildId);
        cfg.setProperty("ListencastAnnounce", listencastAnnounce);
        cfg.store(new FileOutputStream("conf/bot.prop"), comment);
        log.info("Your configuration file has been generated/updated!");
        System.exit(0);
    }
}

