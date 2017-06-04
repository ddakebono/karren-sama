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


import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.InteractionProcessor;
import org.frostbite.karren.interactions.Tag;
import org.frostbite.karren.interactions.Tags.*;
import org.frostbite.karren.interactions.Tags.D4JPlayer.*;
import org.frostbite.karren.interactions.Tags.InstantReplay.Playback;
import org.frostbite.karren.interactions.Tags.InstantReplay.StartListening;
import org.frostbite.karren.interactions.Tags.InstantReplay.StopListening;
import org.frostbite.karren.listeners.InteractionCommands;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private Map<String, Tag> handlers;
    private Map<String, InteractionProcessor> registeredGuilds = new HashMap<>();

    //Default processor handles private message interactions
    private InteractionProcessor defaultProcessor;
    private ArrayList<Interaction> defaultInteractions;
    private boolean lock = false;

    public void loadTags(){
        handlers = new HashMap<>();
        handlers.put("depart", new Depart());
        handlers.put("echo", new Echo());
        handlers.put("interactionreload", new InteractionReload());
        handlers.put("name", new Name());
        handlers.put("pm", new PM());
        handlers.put("random", new Random());
        handlers.put("return", new Return());
        handlers.put("version", new Version());
        handlers.put("overridechannel", new OverrideChannel());
        handlers.put("parameter", new Parameter());
        handlers.put("count5", new Count5());
        handlers.put("disableinteraction", new DisableInteraction());
        handlers.put("enableinteraction", new EnableInteraction());
        handlers.put("count", new Count());
        handlers.put("overwatchprofile", new OverwatchUAPIProfile());
        handlers.put("setstatus", new SetStatus());
        handlers.put("overwatchhero", new OverwatchUAPIHero());
        handlers.put("overwatchallheroes", new OverwatchUAPIAllHeroes());
        handlers.put("deletemsg", new DeleteMessage());
        handlers.put("d4jplay", new D4JPlay());
        handlers.put("d4jlist", new D4JList());
        handlers.put("d4jnowplaying", new D4JNowPlaying());
        handlers.put("d4jshuffle", new D4JShuffle());
        handlers.put("d4jskip", new D4JSkip());
        handlers.put("d4jstop", new D4JStop());
        handlers.put("osugetuser", new OsuGetUser());
        handlers.put("usetts", new TextToSpeach());
        handlers.put("volume", new D4JVolume());
        handlers.put("d4jrepeat", new D4JRepeat());
        handlers.put("d4jsearch", new D4JSearch());
        handlers.put("d4jselect", new D4JSelect());
        handlers.put("d4jnowplayingtime", new D4JNowPlayingTime());
        handlers.put("irstop", new StopListening());
        handlers.put("irstart", new StartListening());
        handlers.put("mentioned", new MentionedUsers());
        handlers.put("irplay", new Playback());
        handlers.put("setfilter", new SetFilter());
        handlers.put("setprefix", new SetPrefix());
        handlers.put("roleroll", new RoleRoll());
        handlers.put("setdifficulty", new SetDifficulty());
    }

    public void loadDefaultInteractions(){
        lock = true;
        if(registeredGuilds.size()>0)
            registeredGuilds.clear();
        Gson gson = new Gson();
        defaultInteractions = new ArrayList<>();
        File intDir = new File("conf/Interactions");
        if(intDir.isDirectory()){
            File[] intFiles = KarrenUtil.getFilesInFolders(intDir);
            for(File file : intFiles){
                try {
                    Interaction tempInteraction = gson.fromJson(new FileReader(file), Interaction.class);
                    tempInteraction.setIdentifier(FilenameUtils.removeExtension(file.getName()));
                    defaultInteractions.add(tempInteraction);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //Initialize default processor
            defaultProcessor = new InteractionProcessor(null, defaultInteractions);
        } else {
            Karren.log.info("No Interaction detected, interaction system unregistered.");
            Karren.bot.getClient().getDispatcher().unregisterListener(new InteractionCommands());
        }
        lock = false;
    }

    public void removeHandler(String id){
        handlers.remove(id.toLowerCase());
    }

    public void addHandler(String id, Tag tag){
        handlers.put(id.toLowerCase(), tag);
    }

    public Map<String, Tag> getHandlers() {
        return handlers;
    }

    public String getCommandPrefix(IGuild guild){
        if(guild!=null){
            String prefix = Karren.bot.getSql().getGuild(guild).getCommandPrefix();
            if(prefix!=null && prefix.trim().length()>0)
                return prefix;
        }
        return Karren.conf.getCommandPrefix();
    }

    public InteractionProcessor getInteractionProcessor(IGuild guild){
        if(!lock){
            if(guild!=null){
                if (!registeredGuilds.containsKey(guild.getStringID()))
                    registeredGuilds.put(guild.getStringID(), new InteractionProcessor(guild, defaultInteractions));
                return registeredGuilds.getOrDefault(guild.getStringID(), defaultProcessor);
            } else {
                return defaultProcessor;
            }
        }
        return null;
    }
}
