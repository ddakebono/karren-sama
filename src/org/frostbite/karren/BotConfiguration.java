/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

import org.pircbotx.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by frostbite on 3/18/14.
 */
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
    private int djHashGenKey;
    private String nickservPass;
    private String channel;
    private String botname;
    private String hostname;
    private int userCap;
    private final String versionMarker = "v0.5";
    public Object getConfigPayload(String target){
        switch(target.toLowerCase()){
            case "botname":
                return botname;
            case "hostname":
                return hostname;
            case "channel":
                return channel;
            case "nickservpass":
                return nickservPass;
            case "djhashgenkey":
                return djHashGenKey;
            case "icecastmount":
                return icecastMount;
            case "icecastPort":
                return icecastPort;
            case "icecastHost":
                return icecastHost;
            case "icecastadminpass":
                return icecastAdminPass;
            case "icecastadminusername":
                return icecastAdminUsername;
            case "sqlpass":
                return sqlpass;
            case "sqldb":
                return sqldb;
            case "sqlport":
                return sqlport;
            case "sqluser":
                return sqluser;
            case "sqlhost":
                return sqlhost;
            case "usercap":
                return userCap;
            case "version":
                return versionMarker;
            default:
                return false;
        }
    }
    public void initConfig() throws IOException{
        Properties cfg = new Properties();
        File check = new File("conf/bot.prop");
        if(check.isFile()){
            cfg.load(new FileInputStream("conf/bot.prop"));
        } else {
            new File("conf").mkdirs();
        }
        botname = cfg.getProperty("botname", "Karren-sama");
        hostname = cfg.getProperty("hostname", "0.0.0.0");
        sqlhost = cfg.getProperty("sqlhost", "0.0.0.0");
        sqlport = cfg.getProperty("sqlport", "3306");
        sqluser = cfg.getProperty("sqluser", "changeme");
        sqlpass = cfg.getProperty("sqlpass", "changeme");
        sqldb = cfg.getProperty("sqldb", "changeme");
        icecastAdminUsername = cfg.getProperty("icecastAdminUsername", "changeme");
        icecastAdminPass = cfg.getProperty("icecastAdminPass", "changeme");
        icecastHost = cfg.getProperty("icecastHost", "0.0.0.0");
        icecastPort = cfg.getProperty("icecastPort", "8000");
        icecastMount = cfg.getProperty("icecastMount", "changeme.mp3");
        nickservPass = cfg.getProperty("nickservPass", "changeme");
        channel = cfg.getProperty("channel", "#changeme");
        djHashGenKey = Integer.parseInt(cfg.getProperty("djHashGenKey", "1"));
        userCap = Integer.parseInt(cfg.getProperty("maximumUsers", "100"));
        if(!cfg.getProperty("karrenVersion", "0").equalsIgnoreCase(versionMarker)){
            Logging.log("Updating configuration file!", true);
            mkNewConfig();
        }
    }
    public void mkNewConfig() throws IOException {
        Properties cfg = new Properties();
        String comment = "Karren-sama IRC bot properties file.";
        cfg.setProperty("karrenVersion", versionMarker);
        cfg.setProperty("botname", botname);
        cfg.setProperty("hostname", hostname);
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
        cfg.setProperty("nickservPass", nickservPass);
        cfg.setProperty("channel", channel);
        cfg.setProperty("djHashGenKey", String.valueOf(djHashGenKey));
        cfg.setProperty("maximumUsers", String.valueOf(userCap));
        cfg.store(new FileOutputStream("conf/bot.prop"), comment);
        System.out.println("Config file generated! Terminating!");
        Logging.log("Your configuration file has been generated/updated!", false);
        System.exit(0);
    }
}

