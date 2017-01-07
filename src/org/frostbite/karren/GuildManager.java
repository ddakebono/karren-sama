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

    public void loadTags(){
        handlers = new HashMap<>();
        handlers.put("depart", new Depart());
        handlers.put("dj", new DJ());
        handlers.put("echo", new Echo());
        handlers.put("fave", new Fave());
        handlers.put("interactionreload", new InteractionReload());
        handlers.put("name", new Name());
        handlers.put("nowplayingtoggle", new NowPlayingToggle());
        handlers.put("pm", new PM());
        handlers.put("random", new Random());
        handlers.put("return", new Return());
        handlers.put("song", new Song());
        handlers.put("songtime", new SongTime());
        handlers.put("version", new Version());
        handlers.put("overridechannel", new OverrideChannel());
        handlers.put("topic", new Topic());
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

    }

    public void loadDefaultInteractions(){
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

    public InteractionProcessor getInteractionProcessor(IGuild guild){
        return defaultProcessor;
    }

}
