/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import io.github.vrchatapi.VRCUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.listeners.*;
import org.knowm.yank.Yank;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.frostbite.karren.Karren.conf;

public class KarrenBot {
    public JDABuilder clientBuilder;
    public JDA client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public Map<Long, GuildMusicManager> gms;
    public HashMap<Long, Boolean> departedUsers = new HashMap<>();
    public GuildManager ic;
    public boolean isKill = false;
    public YouTube yt;
    public AudioPlayerManager pm;
    public AutoInteraction ar = new AutoInteraction();
    public ChannelMonitor cm = new ChannelMonitor();

    public KarrenBot(JDABuilder clientBuilder){
        this.clientBuilder = clientBuilder;
    }

    public void initDiscord(){
        Karren.log.info("Starting up Lavaplayer...");
        gms = new HashMap<>();
        pm = new DefaultAudioPlayerManager();
        pm.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(pm);
        AudioSourceManagers.registerLocalSource(pm);
        //Continue connecting to discord
        if(Karren.conf.getConnectToDiscord()) {
            clientBuilder.addEventListeners(new ConnectCommand());
            clientBuilder.addEventListeners(new KillCommand());
            clientBuilder.addEventListeners(new ReconnectListener());
            clientBuilder.addEventListeners(new ShutdownListener());
            clientBuilder.addEventListeners(new ResumeListener());
            clientBuilder.addEventListeners(new StatCommand());
            clientBuilder.addEventListeners(new VoiceLeaveListener());
            if(Karren.conf.getEnableInteractions())
                clientBuilder.addEventListeners(new InteractionCommand());
            clientBuilder.addEventListeners(new GuildCreateListener());
            initExtras();
            try {
                client = clientBuilder.build();
            } catch (LoginException e) {
                e.printStackTrace();
            }
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
            yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> { }).setApplicationName("Karren-sama").build();

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
        client.shutdown();
        Karren.bot = null;
        System.gc();
        Karren.log.info("Bot has been killed by " + killer);
        System.exit(0);
    }

    public void onConnectStartup(){
        //Initialize database connection pool
        Karren.log.info("Initializing Yank database pool");
        Properties dbSettings = new Properties();
        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", conf.getSqluser());
        dbSettings.setProperty("password", conf.getSqlpass());

        if(Karren.conf.getAllowSQLRW())
            Yank.setupDefaultConnectionPool(dbSettings);

        //Launch threads
        Karren.bot.ar.start();
        Karren.bot.cm.start();

        if(!Karren.conf.isTestMode()) {
            if(Karren.conf.getStatusOverride().isEmpty())
                client.getPresence().setActivity(Activity.playing("KarrenSama Ver." + Karren.botVersion));
            else
                client.getPresence().setActivity(Activity.playing(Karren.conf.getStatusOverride()));
        } else {
            client.getPresence().setActivity(Activity.playing("TEST MODE - " + Karren.botVersion));
        }
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

    public JDA getClient(){
        return client;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild){
        return gms.get(guild.getIdLong());
    }

    public void createGuildMusicManager(Guild guild){
        if(!gms.containsKey(guild.getIdLong())) {
            gms.put(guild.getIdLong(), new GuildMusicManager(pm, guild));
            guild.getAudioManager().setSendingHandler(getGuildMusicManager(guild).getAudioProvider());
        }
    }

    public AudioPlayerManager getPm() {
        return pm;
    }

    public AutoInteraction getAr() {
        return ar;
    }

    public ChannelMonitor getCm() {
        return cm;
    }
}
