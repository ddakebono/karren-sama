/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import io.github.vrchatapi.VRCUser;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.InstantReplay.InstantReplayManager;
import org.frostbite.karren.listeners.*;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.HashMap;
import java.util.Map;

public class KarrenBot {
    public IDiscordClient client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public GuildManager ic;
    public boolean isKill = false;
    public Map<String, GuildMusicManager> gms;
    public InstantReplayManager irm;
    public AudioPlayerManager pm = new DefaultAudioPlayerManager();
    public AutoInteraction ar = new AutoInteraction();
    public InteractionCommands interactionListener = new InteractionCommands();
    public ChannelMonitor cm = new ChannelMonitor();
    public YouTube yt;
    public TwitchClient ttv;

    public KarrenBot(IDiscordClient client){
        this.client = client;
    }

    public void initDiscord(){
        //Init lavaplayer
        Karren.log.info("Starting up Lavaplayer...");
        gms = new HashMap<>();
        AudioSourceManagers.registerRemoteSources(pm);
        AudioSourceManagers.registerLocalSource(pm);
        //Continue connecting to discord
        if(Karren.conf.getConnectToDiscord()) {
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
            //ed.registerListener(new StatusListener());
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
            irm = new InstantReplayManager();

            //Setup twitch
            ttv = TwitchClientBuilder.builder().withEnableHelix(true).withClientId(Karren.conf.getTwitchIDKey()).withClientSecret(Karren.conf.getTwitchAPIKey()).build();

            //Setup youtube
            yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> { }).setApplicationName("Karren-sama").build();

            //Log into VRCAPI and get auth token
            VRCUser.login(Karren.conf.getVrcUsername(), Karren.conf.getVrcPassword());

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

    public GuildMusicManager getGuildMusicManager(IGuild guild){
        return gms.get(guild.getStringID());
    }

    public GuildMusicManager createGuildMusicManager(IGuild guild){
        if(!gms.containsKey(guild.getStringID()))
            gms.put(guild.getStringID(), new GuildMusicManager(pm, guild));
        return gms.get(guild.getStringID());
    }

    public AudioPlayerManager getPm() {
        return pm;
    }

    public InstantReplayManager getIrm() {
        return irm;
    }

    public AutoInteraction getAr() {
        return ar;
    }

    public ChannelMonitor getCm() {
        return cm;
    }
}
