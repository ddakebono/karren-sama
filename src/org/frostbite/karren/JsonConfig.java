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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


@JsonIgnoreProperties({"discordToken", "set"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class JsonConfig {

    private String confVersionMarker = "";
    private boolean connectToDiscord = true;
    private boolean allowSQLRW = true;
    private boolean enableInteractions = true;
    private String commandPrefix = ".";
    private String sqlhost = "0.0.0.0";
    private int sqlport = 3306;
    private String sqldb = "changeme";
    private String sqluser = "changeme";
    private String sqlpass = "changeme";
    private String icecastAdminUsername = "changeme";
    private String icecastAdminPass = "changeme";
    private boolean listencastAnnounce = false;
    private boolean enableListencast = true;
    private String icecastMount = "changeme.ogg";
    private String icecastHost = "0.0.0.0";
    private int icecastPort = 8000;
    private String discordApiKey = "changeme";
    private String osuAPIKey = "changeme";
    private String googleAPIKey = "changeme";

    public JsonConfig(String confVersionMarker) {
        this.confVersionMarker = confVersionMarker;
    }

    @JsonCreator
    public JsonConfig(@JsonProperty("confVersionMarker") String confVersionMarker,@JsonProperty("connectToDiscord") boolean connectToDiscord,@JsonProperty("allowSQLRW") boolean allowSQLRW,@JsonProperty("enableInteractions") boolean enableInteractions,@JsonProperty("commandPrefix") String commandPrefix,@JsonProperty("sqlhost") String sqlhost,@JsonProperty("sqlport") int sqlport,@JsonProperty("sqldb") String sqldb,@JsonProperty("sqluser") String sqluser,@JsonProperty("sqlpass") String sqlpass,@JsonProperty("icecastAdminUsername") String icecastAdminUsername,@JsonProperty("icecastAdminPass") String icecastAdminPass,@JsonProperty("listencastAnnounce") boolean listencastAnnounce,@JsonProperty("enableListencast") boolean enableListencast,@JsonProperty("icecastMount") String icecastMount,@JsonProperty("icecastHost") String icecastHost,@JsonProperty("icecastPort") int icecastPort,@JsonProperty("discordApiKey") String discordApiKey,@JsonProperty("osuAPIKey") String osuAPIKey,@JsonProperty("googleAPIKey") String googleAPIKey) {
        this.confVersionMarker = confVersionMarker;
        this.connectToDiscord = connectToDiscord;
        this.allowSQLRW = allowSQLRW;
        this.enableInteractions = enableInteractions;
        this.commandPrefix = commandPrefix;
        this.sqlhost = sqlhost;
        this.sqlport = sqlport;
        this.sqldb = sqldb;
        this.sqluser = sqluser;
        this.sqlpass = sqlpass;
        this.icecastAdminUsername = icecastAdminUsername;
        this.icecastAdminPass = icecastAdminPass;
        this.listencastAnnounce = listencastAnnounce;
        this.enableListencast = enableListencast;
        this.icecastMount = icecastMount;
        this.icecastHost = icecastHost;
        this.icecastPort = icecastPort;
        this.discordApiKey = discordApiKey;
        this.osuAPIKey = osuAPIKey;
        this.googleAPIKey = googleAPIKey;
    }

    public boolean isSet(){
        return !connectToDiscord || !discordApiKey.equalsIgnoreCase("changeme");
    }

    public boolean checkUpdate(boolean noFile){
        if(noFile || !confVersionMarker.equalsIgnoreCase(Karren.confVersion)){
            Karren.log.info("Updating configuration file...");
            saveConfig();
            return true;
        }
        return false;
    }

    public void loadConfig(){
        Karren.log.info("Loading config from file...");
        ObjectMapper json = new ObjectMapper();
        try {
            Karren.conf = json.readValue(new File("conf/bot.json"), JsonConfig.class);
            Karren.log.info("Successfully loaded config file!");
        } catch (IOException e) {
            e.printStackTrace();
            checkUpdate(true);
        }
    }

    public void saveConfig(){
        ObjectMapper json = new ObjectMapper();
        try {
            json.writerWithDefaultPrettyPrinter().writeValue(new File("conf/bot.json"), this);
            Karren.log.info("Successfully saved config file!");
        } catch (IOException e) {
            Karren.log.error("Could not save bot.json config file, please check folder permissions and retry.");
            e.printStackTrace();
        }
    }

    public boolean getConnectToDiscord() {
        return connectToDiscord;
    }

    public boolean getAllowSQLRW() {
        return allowSQLRW;
    }

    public boolean getEnableInteractions() {
        return enableInteractions;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public String getSqlhost() {
        return sqlhost;
    }

    public int getSqlport() {
        return sqlport;
    }

    public String getSqldb() {
        return sqldb;
    }

    public String getSqluser() {
        return sqluser;
    }

    public String getSqlpass() {
        return sqlpass;
    }

    public String getIcecastAdminUsername() {
        return icecastAdminUsername;
    }

    public String getIcecastAdminPass() {
        return icecastAdminPass;
    }

    public boolean getListencastAnnounce() {
        return listencastAnnounce;
    }

    public boolean getEnableListencast() {
        return enableListencast;
    }

    public String getIcecastMount() {
        return icecastMount;
    }

    public String getIcecastHost() {
        return icecastHost;
    }

    public int getIcecastPort() {
        return icecastPort;
    }

    public String getDiscordToken() {
        return discordApiKey;
    }

    public String getOsuAPIKey() {
        return osuAPIKey;
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }
}
