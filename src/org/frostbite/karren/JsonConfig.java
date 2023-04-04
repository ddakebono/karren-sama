/*
 * Copyright (c) 2023 Owen Bennett.
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

    private String confVersionMarker;
    private boolean connectToDiscord = true;
    private boolean allowSQLRW = true;
    private boolean enableInteractions = true;
    private String commandPrefix = ".";
    private String sqlhost = "0.0.0.0";
    private int sqlport = 3306;
    private String sqldb = "changeme";
    private String sqluser = "changeme";
    private String sqlpass = "changeme";
    private String discordApiKey = "changeme";
    private String googleAPIKey = "changeme";
    private String operatorDiscordID = "changeme";
    private String statusOverride = "";
    private boolean testMode = false;

    public JsonConfig(String confVersionMarker) {
        this.confVersionMarker = confVersionMarker;
    }

    @JsonCreator
    public JsonConfig(@JsonProperty("confVersionMarker") String confVersionMarker,@JsonProperty("connectToDiscord") boolean connectToDiscord,@JsonProperty("allowSQLRW") boolean allowSQLRW,@JsonProperty("enableInteractions") boolean enableInteractions,@JsonProperty("commandPrefix") String commandPrefix,@JsonProperty("sqlhost") String sqlhost,@JsonProperty("sqlport") int sqlport,@JsonProperty("sqldb") String sqldb,@JsonProperty("sqluser") String sqluser,@JsonProperty("sqlpass") String sqlpass,@JsonProperty("discordApiKey") String discordApiKey,@JsonProperty("googleAPIKey") String googleAPIKey, @JsonProperty("operatorDiscordID")String operatorDiscordID, @JsonProperty("TestMode")boolean testMode, @JsonProperty("statusOverride") String statusOverride) {
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
        this.discordApiKey = discordApiKey;
        this.googleAPIKey = googleAPIKey;
        this.statusOverride = statusOverride;
        this.operatorDiscordID = operatorDiscordID;
        this.testMode = testMode;
    }

    public boolean isSet(){
        return !connectToDiscord || !discordApiKey.equalsIgnoreCase("changeme");
    }

    public boolean checkUpdate(boolean noFile){
        if(noFile || !confVersionMarker.equalsIgnoreCase(Karren.confVersion)){
            Karren.log.info("Updating configuration file...");
            confVersionMarker = Karren.confVersion;
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

    public String getDiscordToken() {
        return discordApiKey;
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }

    public String getOperatorDiscordID() {
        return operatorDiscordID;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public String getStatusOverride() {
        return statusOverride;
    }
}
