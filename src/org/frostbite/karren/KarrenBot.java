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

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.Interactions.TagHelperClasses.DepartedUser;
import org.frostbite.karren.listeners.*;
import org.knowm.yank.Yank;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KarrenBot {
    public JDABuilder clientBuilder;
    public JDA client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public Map<Long, GuildMusicManager> gms;
    public List<DepartedUser> departedUsers = new LinkedList<>();
    public GuildManager ic;
    public boolean isKill = false;
    public YouTube yt;
    public AudioPlayerManager pm;
    public AutoInteraction ar = new AutoInteraction();

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
            if(!Karren.conf.isTestMode())
                clientBuilder.setActivity(Activity.playing("KarrenSama Ver." + Karren.botVersion));
            else
                clientBuilder.setActivity(Activity.playing("TEST MODE - " + Karren.botVersion));
            clientBuilder.addEventListeners(new ConnectCommand());
            clientBuilder.addEventListeners(new KillCommand());
            clientBuilder.addEventListeners(new ReconnectListener());
            clientBuilder.addEventListeners(new ShutdownListener());
            clientBuilder.addEventListeners(new StatCommand());
            clientBuilder.addEventListeners(new InteractionCommand());
            clientBuilder.addEventListeners(new GuildCreateListener());
            initExtras();
            try {
                client = clientBuilder.build();
            } catch (LoginException e) {
                e.printStackTrace();
            }
            //client.login().block(); //Let's GOOOOO
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
            //VRCUser.login(Karren.conf.getVrcUsername(), Karren.conf.getVrcPassword());

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
