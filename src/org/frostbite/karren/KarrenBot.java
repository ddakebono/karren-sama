/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import com.google.api.services.youtube.YouTube;
import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ConnectEvent;
import discord4j.core.event.domain.lifecycle.DisconnectEvent;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.vrchatapi.VRCUser;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.listeners.*;
import org.knowm.yank.Yank;

public class KarrenBot {
    public DiscordClient client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public GuildManager ic;
    public boolean isKill = false;
    public YouTube yt;

    public KarrenBot(DiscordClient client){
        this.client = client;
    }

    public void initDiscord(){
        //TODO lavaplayer
        //Continue connecting to discord
        if(Karren.conf.getConnectToDiscord()) {
            EventDispatcher ed = client.getEventDispatcher();
            ed.on(ConnectEvent.class).subscribe(new ConnectCommand());
            ed.on(MessageCreateEvent.class).subscribe(new KillCommand());
            ed.on(ReconnectEvent.class).subscribe(new ReconnectListener());
            ed.on(DisconnectEvent.class).subscribe(new ShutdownListener());
            ed.on(MessageCreateEvent.class).subscribe(new StatCommand());
            ed.on(MessageCreateEvent.class).subscribe(new InteractionCommand());
            /*ed.registerListener(new ConnectCommand());
            ed.registerListener(new HelpCommand());
            ed.registerListener(interactionListener);
            ed.registerListener(new KillCommand());
            ed.registerListener(new GuildCreateListener());
            ed.registerListener(new ShutdownListener());
            ed.registerListener(new ReconnectListener());
            ed.registerListener(new StatCommand());*/
            initExtras();
            client.login().block(); //Let's GOOOOO
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            //Init interaction processor

        }
    }

    public void initExtras(){
        if(!extrasReady) {
            ic = new GuildManager();
            ic.loadDefaultInteractions();

            //Setup youtube
            //yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> { }).setApplicationName("Karren-sama").build();

            //Log into VRCAPI and get auth token
            VRCUser.login(Karren.conf.getVrcUsername(), Karren.conf.getVrcPassword());

            extrasReady = true;
        }
    }

    public void killBot(String killer){
        Karren.bot.isKill = true;
        //Unhook and shutdown interaction system
        Yank.releaseAllConnectionPools();
        //Interactions reset to default state and unregistered
        client.logout();
        Karren.bot = null;
        System.gc();
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

    public DiscordClient getClient(){
        return client;
    }

    public void setClient(DiscordClient client) {
        this.client = client;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

    /*public GuildMusicManager getGuildMusicManager(Guild guild){
        return gms.get(guild.getStringID());
    }

    public GuildMusicManager createGuildMusicManager(Guild guild){
        if(!gms.containsKey(guild.getStringID()))
            gms.put(guild.getStringID(), new GuildMusicManager(pm, guild));
        return gms.get(guild.getStringID());
    }*/

    //TODO Audio player
    /*public AudioPlayerManager getPm() {
        return pm;
    }*/

    //TODO Instant Replay
    /*public InstantReplayManager getIrm() {
        return irm;
    }*/

    //TODO Auto interaction
    /*public AutoInteraction getAr() {
        return ar;
    }*/

    //TODO Channel Monitor
    /*public ChannelMonitor getCm() {
        return cm;
    }*/
}
