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

import org.frostbite.karren.InterConnect.InterConnectListener;
import org.frostbite.karren.interactions.InteractionManager;
import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.ConnectCommand;
import org.frostbite.karren.listeners.HelpCommand;
import org.frostbite.karren.listeners.InteractionCommands;
import org.frostbite.karren.listeners.KillCommand;
import sx.blah.discord.api.EventDispatcher;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

public class KarrenBot {
    IDiscordClient client;
    MySQLInterface sql = new MySQLInterface();
    ListenCast lc;
    boolean extrasReady = false;
    InteractionManager ic;
    InterConnectListener interConnectListener;

    public KarrenBot(IDiscordClient client){
        this.client = client;
    }

    public void initDiscord(){
        if(Boolean.parseBoolean(Karren.conf.getConnectToDiscord())) {
            EventDispatcher ed = client.getDispatcher();
            ed.registerListener(new ConnectCommand());
            ed.registerListener(new HelpCommand());
            ed.registerListener(new InteractionCommands());
            ed.registerListener(new KillCommand());
            try {
                client.login();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            startThreads();
        }
    }

    public void initExtras(){
        ic = new InteractionManager();
        ic.loadTags();
        ic.loadInteractions();
        lc = new ListenCast(client);
        interConnectListener = new InterConnectListener(Karren.log);
        extrasReady = true;
    }

    public boolean startThreads(){
        if(extrasReady) {
            lc.start();
            interConnectListener.start();
            return true;
        } else {
            return false;
        }
    }

    public void killBot(String killer){
        lc.kill();
        try {
            client.logout();
        } catch (HTTP429Exception | DiscordException e) {
            e.printStackTrace();
        }
        Karren.log.info("Bot has been killed by " + killer);
        System.exit(0);
    }

    /*
    GETTERS
     */

    public MySQLInterface getSql(){
        return sql;
    }

    public InteractionManager getInteractionManager() {return ic;}

    public IDiscordClient getClient(){
        return client;
    }

    public ListenCast getListenCast(){
        return lc;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }
}
