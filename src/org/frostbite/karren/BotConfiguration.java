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
    private int sqlport;
    private String sqluser;
    private String sqldb;
    private String sqlpass;
    private String icecastAdminUsername;
    private String icecastAdminPass;
    private String icecastHost;
    private int icecastPort;
    private String icecastMount;
    private String discordToken;
    private boolean allowSQLRW;
    private boolean enableListencast;
    private boolean listencastAnnounce;
    private boolean enableInteractions;
    private String commandPrefix;
    private boolean connectToDiscord;
    private final String versionMarker = "3.2";
    private String osuAPIKey;
    private String googleAPIKey;
    /*
    Config Getters
     */

    public String getDiscordToken() {
        return discordToken;
    }
    public boolean getListencastAnnounce() {
        return listencastAnnounce;
    }
    public String getSqlhost() {
        return sqlhost;
    }
    public int getSqlport() {
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
    public boolean getConnectToDiscord() {return connectToDiscord;}
    public String getIcecastMount() {
        return icecastMount;
    }
    public int getIcecastPort() {
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
    public boolean getAllowSQLRW() {
        return allowSQLRW;
    }
    public boolean getEnableListencast() {
        return enableListencast;
    }
    public boolean getEnableInteractions() {
        return enableInteractions;
    }
    public String getCommandPrefix() { return commandPrefix; }
    public String getOsuAPIKey() {return osuAPIKey;}
    public String getGoogleAPIKey() {
        return googleAPIKey;
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
        sqlport = Integer.parseInt(cfg.getProperty("sqlport", "3306"));
        sqluser = cfg.getProperty("sqluser", "changeme");
        sqlpass = cfg.getProperty("sqlpass", "changeme");
        sqldb = cfg.getProperty("sqldb", "changeme");
        icecastAdminUsername = cfg.getProperty("icecastAdminUsername", "changeme");
        icecastAdminPass = cfg.getProperty("icecastAdminPass", "changeme");
        icecastHost = cfg.getProperty("icecastHost", "0.0.0.0");
        icecastPort = Integer.parseInt(cfg.getProperty("icecastPort", "8000"));
        icecastMount = cfg.getProperty("icecastMount", "changeme.ogg");
        allowSQLRW = Boolean.parseBoolean(cfg.getProperty("allowSQLReadWrite", "true"));
        enableInteractions = Boolean.parseBoolean(cfg.getProperty("enableInteractionSystem", "true"));
        enableListencast = Boolean.parseBoolean(cfg.getProperty("enableListencastSystem", "true"));
        commandPrefix = cfg.getProperty("commandPrefix", ".");
        connectToDiscord = Boolean.parseBoolean(cfg.getProperty("connectToDiscord", "true"));
        discordToken = cfg.getProperty("BotAccountToken", "hackme");
        osuAPIKey = cfg.getProperty("osuAPIKey", "");
        googleAPIKey = cfg.getProperty("googleAPIKey", "");
        listencastAnnounce = Boolean.parseBoolean(cfg.getProperty("ListencastAnnounce", "true"));
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
        cfg.setProperty("sqlport", Integer.toString(sqlport));
        cfg.setProperty("sqluser", sqluser);
        cfg.setProperty("sqlpass", sqlpass);
        cfg.setProperty("sqldb", sqldb);
        cfg.setProperty("icecastAdminUsername", icecastAdminUsername);
        cfg.setProperty("icecastAdminPass", icecastAdminPass);
        cfg.setProperty("icecastHost", icecastHost);
        cfg.setProperty("icecastPort", Integer.toString(icecastPort));
        cfg.setProperty("icecastMount", icecastMount);
        cfg.setProperty("allowSQLReadWrite", Boolean.toString(allowSQLRW));
        cfg.setProperty("enableInteractionSystem", Boolean.toString(enableInteractions));
        cfg.setProperty("enableListencastSystem", Boolean.toString(enableListencast));
        cfg.setProperty("commandPrefix", commandPrefix);
        cfg.setProperty("connectToDiscord", Boolean.toString(connectToDiscord));
        cfg.setProperty("BotAccountToken", discordToken);
        cfg.setProperty("osuAPIKey", osuAPIKey);
        cfg.setProperty("googleAPIKey", googleAPIKey);
        cfg.setProperty("ListencastAnnounce", Boolean.toString(listencastAnnounce));
        cfg.store(new FileOutputStream("conf/bot.prop"), comment);
        log.info("Your configuration file has been generated/updated!");
        System.exit(0);
    }
}

