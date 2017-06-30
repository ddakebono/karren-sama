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
import org.frostbite.karren.listeners.InteractionCommands;
import org.reflections.Reflections;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private Tag[] tagHandlers;
    private Map<String, InteractionProcessor> registeredGuilds = new HashMap<>();
    //Default processor handles private message interactions
    private InteractionProcessor defaultProcessor;
    private ArrayList<Interaction> defaultInteractions;
    private boolean lock = false;

    public void loadTags(){
        Reflections reflection = new Reflections("org.frostbite.karren.interactions.Tags");
        tagHandlers = (Tag[]) reflection.getSubTypesOf(Tag.class).toArray();
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
                    tempInteraction.setInteractionFile(file);
                    tempInteraction.interactionOldFormatUpdate();
                    defaultInteractions.add(tempInteraction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Initialize default processor
            defaultProcessor = new InteractionProcessor(null, defaultInteractions);
            defaultProcessor.loadAndUpdateDatabase();
        } else {
            Karren.log.info("No Interaction detected, interaction system unregistered.");
            Karren.bot.getClient().getDispatcher().unregisterListener(new InteractionCommands());
        }
        lock = false;
    }

    public Tag getTag(String name){
        return Arrays.stream(tagHandlers).filter(x -> x.getTagName().equalsIgnoreCase(name)).findFirst().orElse(null);
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
