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

public class YamlConfig {
    private final String versionMarker = "3.2";
    private boolean connectToDiscord;
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
    private String osuAPIKey;
    private String googleAPIKey;
}
