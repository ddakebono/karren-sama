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


import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.InteractionProcessor;
import org.frostbite.karren.interactions.Tag;
import org.frostbite.karren.interactions.Tags.*;
import org.frostbite.karren.interactions.Tags.D4JPlayer.*;
import org.frostbite.karren.interactions.Tags.Guild.*;
import org.frostbite.karren.interactions.Tags.InstantReplay.Playback;
import org.frostbite.karren.interactions.Tags.InstantReplay.StartListening;
import org.frostbite.karren.interactions.Tags.InstantReplay.StopListening;
import org.frostbite.karren.interactions.Tags.VRChat.VRCLinkAccount;
import org.frostbite.karren.interactions.Tags.VRChat.VRCUnlinkAccount;
import org.frostbite.karren.interactions.Tags.VRChat.VRCUserSearch;
import org.frostbite.karren.interactions.Tags.VRChat.VRCWorldSearch;
import org.frostbite.karren.listeners.InteractionCommands;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private ArrayList<Tag> tagHandlers;
    private Map<String, InteractionProcessor> registeredGuilds = new HashMap<>();
    //Default processor handles private message interactions
    private InteractionProcessor defaultProcessor;
    private ArrayList<Interaction> defaultInteractions;
    private boolean lock = false;

    public void loadTags(){
        tagHandlers = new ArrayList<>();
        tagHandlers.add(new Depart());
        tagHandlers.add(new Echo());
        tagHandlers.add(new InteractionReload());
        tagHandlers.add(new Name());
        tagHandlers.add(new PM());
        tagHandlers.add(new Random());
        tagHandlers.add(new Return());
        tagHandlers.add(new Version());
        tagHandlers.add(new OverrideChannel());
        tagHandlers.add(new Parameter());
        tagHandlers.add(new Count5());
        tagHandlers.add(new DisableInteraction());
        tagHandlers.add(new EnableInteraction());
        tagHandlers.add(new Count());
        tagHandlers.add(new OverwatchUAPIProfile());
        tagHandlers.add(new OverwatchUAPIHero());
        tagHandlers.add(new OverwatchUAPIAllHeroes());
        tagHandlers.add(new DeleteMessage());
        tagHandlers.add(new D4JPlay());
        tagHandlers.add(new D4JList());
        tagHandlers.add(new D4JNowPlaying());
        tagHandlers.add(new D4JShuffle());
        tagHandlers.add(new D4JSkip());
        tagHandlers.add(new D4JStop());
        tagHandlers.add(new OsuGetUser());
        tagHandlers.add(new TextToSpeach());
        tagHandlers.add(new D4JVolume());
        tagHandlers.add(new D4JRepeat());
        tagHandlers.add(new D4JSearch());
        tagHandlers.add(new D4JSelect());
        tagHandlers.add(new D4JNowPlayingTime());
        tagHandlers.add(new StopListening());
        tagHandlers.add(new StartListening());
        tagHandlers.add(new MentionedUsers());
        tagHandlers.add(new Playback());
        tagHandlers.add(new SetFilter());
        tagHandlers.add(new SetPrefix());
        tagHandlers.add(new RoleRoll());
        tagHandlers.add(new SetDifficulty());
        tagHandlers.add(new ReminderCheck());
        tagHandlers.add(new ReminderAdd());
        tagHandlers.add(new VoiceChannelRequired());
        tagHandlers.add(new RollTopGuilds());
        tagHandlers.add(new WatchdogEvent());
        tagHandlers.add(new NoVoiceHijack());
        tagHandlers.add(new EmbedMessage());
        tagHandlers.add(new CreateTempChannel());
        tagHandlers.add(new D4JFeelingLucky());
        tagHandlers.add(new SetMaxVolume());
        tagHandlers.add(new GetGuildName());
        tagHandlers.add(new GuildSettings());
        tagHandlers.add(new SetRandomRange());
        tagHandlers.add(new SetOverrideChannel());
        tagHandlers.add(new VRCUserSearch());
        tagHandlers.add(new VRCLinkAccount());
        tagHandlers.add(new VRCWorldSearch());
        tagHandlers.add(new VRCUnlinkAccount());
    }

    public ArrayList<Interaction> loadInteractions(){
        ArrayList<Interaction> loadedInteractions = new ArrayList<>();
        Gson gson = new Gson();
        File intDir = new File("conf/Interactions");
        if(intDir.isDirectory()){
            //Setup watchdog interaction
            loadedInteractions.add(new Interaction("WatchdogSpecial", new String[]{"watchdog", "nodisplay", "prefixed"}, "", new String[]{"watchdogtestevent"}, 1, true, ""));
            File[] intFiles = KarrenUtil.getFilesInFolders(intDir);
            for(File file : intFiles){
                try {
                    Interaction tempInteraction = gson.fromJson(new FileReader(file), Interaction.class);
                    tempInteraction.setIdentifier(FilenameUtils.removeExtension(file.getName()));
                    tempInteraction.setInteractionFile(file);
                    tempInteraction.interactionOldFormatUpdate();
                    tempInteraction.prepareOrUpdateItems(); //Currency system
                    loadedInteractions.add(tempInteraction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Karren.log.info("No Interactions detected, interaction system unregistered.");
            Karren.bot.getClient().getDispatcher().unregisterListener(new InteractionCommands());
            return null;
        }
        return loadedInteractions;
    }

    public void loadDefaultInteractions(){
        lock = true;
        if(registeredGuilds.size()>0)
            registeredGuilds.clear();
        defaultInteractions = loadInteractions();
        //Initialize default processor
        if(defaultInteractions!=null)
            defaultProcessor = new InteractionProcessor(null, defaultInteractions);
        lock = false;
    }

    public void clearGuildInteractionProcessor(IGuild guild){
        if(guild!=null){
            if(registeredGuilds.containsKey(guild.getStringID())){
                registeredGuilds.remove(guild.getStringID());
            }
        }
    }

    public Tag getTag(String name){
        Tag tag = tagHandlers.stream().filter(x -> x.getTagName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if(tag!=null) {
            try {
                //Create a new instance of a tag for each operation
                return tag.getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getCommandPrefix(IGuild guild){
        if(guild!=null && Karren.conf.getAllowSQLRW()){
            String prefix = Karren.bot.getSql().getGuild(guild).getCommandPrefix();
            if(prefix!=null && prefix.trim().length()>0)
                return prefix;
        }
        return Karren.conf.getCommandPrefix();
    }

    public InteractionProcessor getInteractionProcessor(IGuild guild){
        if(!lock && defaultInteractions.size()>0){
            if(guild!=null){
                if (!registeredGuilds.containsKey(guild.getStringID())) {
                    ArrayList<Interaction> loadedInteractions = loadInteractions();
                    registeredGuilds.put(guild.getStringID(), new InteractionProcessor(guild, loadedInteractions));
                }
                return registeredGuilds.getOrDefault(guild.getStringID(), defaultProcessor);
            } else {
                return defaultProcessor;
            }
        }
        return null;
    }

    public ArrayList<Tag> getTagHandlers() {
        return tagHandlers;
    }
    public Map<String, InteractionProcessor> getRegisteredGuilds() { return registeredGuilds; }
}
