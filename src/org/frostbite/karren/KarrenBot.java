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
import org.frostbite.karren.listeners.*;
import org.pircbotx.PircBotX;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class KarrenBot {
    public PircBotX client;
    //public IDiscordClient client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public GuildManager ic;
    public boolean isKill = false;
    public AutoInteraction ar = new AutoInteraction();
    public InteractionCommands interactionListener = new InteractionCommands();
    public ChannelMonitor cm = new ChannelMonitor();

    public KarrenBot(PircBotX client){
        this.client = client;
    }

    public void initDiscord(){
        //Continue connecting to discord
        if(Karren.conf.getConnectToDiscord()) {
            client.
            EventDispatcher ed = client.getDispatcher();
            ed.registerListener(new ConnectCommand());
            try {
                client.login();
            } catch (DiscordException | RateLimitException e) {
                e.printStackTrace();
            }
            ed.registerListener(new HelpCommand());
            ed.registerListener(interactionListener);
            ed.registerListener(new KillCommand());
            ed.registerListener(new GuildCreateListener());
            ed.registerListener(new ShutdownListener());
            ed.registerListener(new ReconnectListener());
            ed.registerListener(new StatCommand());
            initExtras();
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            //Init interaction processor

        }
    }

    public void initExtras(){
        if(!extrasReady) {
            ic = new GuildManager();
            ic.loadTags();
            ic.loadDefaultInteractions();
            extrasReady = true;
        }
    }

    public void killBot(String killer){
        Karren.watchdog.cleanupBot();
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

    public IDiscordClient getClient(){
        return client;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

    public AutoInteraction getAr() {
        return ar;
    }

    public ChannelMonitor getCm() {
        return cm;
    }
}
