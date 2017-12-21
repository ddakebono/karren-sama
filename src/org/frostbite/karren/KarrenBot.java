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

import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.listeners.InteractionCommands;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class KarrenBot {
    public PircBotX client;
    //public IDiscordClient client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public GuildManager ic;
    public boolean isKill = false;
    public InteractionCommands interactionListener = new InteractionCommands();

    public KarrenBot(PircBotX client){
        this.client = client;
    }

    public void initDiscord() throws IOException, IrcException {
        //Continue connecting to discord
        if(Karren.conf.getConnectToDiscord()) {
            client.startBot();
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            //Init interaction processor

        }
    }

    public void initExtras(){
        if(!extrasReady) {
            Karren.log.debug("Starting up interaction system and guild manager");
            ic = new GuildManager();
            ic.loadTags();
            ic.loadDefaultInteractions();
            extrasReady = true;
        }
    }

    public void killBot(String killer){
        client.stopBotReconnect();
        client.sendIRC().quitServer("Kill command given, shutting down!");
        Karren.log.info("Bot has been killed by " + killer);
        System.exit(0);
    }

    /*
    GETTERS
     */

    public boolean isKill() {
        return isKill;
    }

    public MySQLInterface getSql(){
        return sql;
    }

    public GuildManager getGuildManager() {return ic;}

    public PircBotX getClient(){
        return client;
    }

    public void setClient(PircBotX client) {
        this.client = client;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

}
