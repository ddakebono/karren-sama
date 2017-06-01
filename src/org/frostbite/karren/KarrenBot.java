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

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.InstantReplay.InstantReplayManager;
import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.*;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.HashMap;
import java.util.Map;

public class KarrenBot {
    private IDiscordClient client;
    private MySQLInterface sql = new MySQLInterface();
    private ListenCast lc;
    private boolean extrasReady = false;
    private GuildManager ic;
    private boolean isKill = false;
    private Map<String, GuildMusicManager> gms;
    private InstantReplayManager irm;
    private AudioPlayerManager pm = new DefaultAudioPlayerManager();

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
            ed.registerListener(new HelpCommand());
            ed.registerListener(new InteractionCommands());
            ed.registerListener(new KillCommand());
            ed.registerListener(new GuildCreateListener());
            ed.registerListener(new ShutdownListener());
            try {
                client.login();
            } catch (DiscordException | RateLimitException e) {
                e.printStackTrace();
            }
            initExtras();
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            startThreads();
        }
    }

    public void initExtras(){
        if(!extrasReady) {
            ic = new GuildManager();
            ic.loadTags();
            ic.loadDefaultInteractions();
            lc = new ListenCast(client);
            irm = new InstantReplayManager();
            extrasReady = true;
        }
    }

    public boolean startThreads(){
        if(extrasReady) {
            if(!lc.isAlive())
                lc.start();
            return true;
        } else {
            return false;
        }
    }

    public void killBot(String killer){
        isKill = true;
        lc.kill();
        if(client.isReady()) {
            try {
                client.logout();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
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

    public ListenCast getListenCast(){
        return lc;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

    public GuildMusicManager getGuildMusicManager(IGuild guild){
        return gms.get(guild.getStringID());
    }

    public GuildMusicManager createGuildMusicManager(IGuild guild){
        gms.put(guild.getStringID(), new GuildMusicManager(pm, guild));
        return gms.get(guild.getStringID());
    }

    public AudioPlayerManager getPm() {
        return pm;
    }

    public InstantReplayManager getIrm() {
        return irm;
    }
}
