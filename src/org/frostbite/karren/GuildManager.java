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


import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionProcessor;
import org.frostbite.karren.Interactions.Tag;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuildManager {

    private Map<String, InteractionProcessor> registeredGuilds = new HashMap<>();
    //Default processor handles private message interactions
    private InteractionProcessor defaultProcessor;
    private ArrayList<Interaction> defaultInteractions;
    private boolean lock = false;

    public ArrayList<Interaction> loadInteractions(boolean standardOnly){
        ArrayList<Interaction> loadedInteractions = new ArrayList<>();
        Gson gson = new Gson();
        File intDir = new File("conf/Interactions");
        if(intDir.isDirectory()){
            //Setup watchdog interaction
            File[] intFiles = KarrenUtil.getFilesInFolders(intDir);
            for(File file : intFiles){
                try {
                    Interaction tempInteraction = gson.fromJson(new FileReader(file), Interaction.class);
                    tempInteraction.setIdentifier(FilenameUtils.removeExtension(file.getName()));
                    tempInteraction.setInteractionFile(file);
                    if(!(standardOnly && tempInteraction.isGuildOnly()))
                    loadedInteractions.add(tempInteraction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Karren.log.info("No Interactions detected, interaction system unregistered.");
            //Karren.bot.getClient().getEventDispatcher().unregisterListener(new InteractionCommands());
            //Karren.bot.getClient().getEventDispatcher().on(MessageCreateEvent.class)
            return null;
        }
        return loadedInteractions;
    }

    public void loadDefaultInteractions(){
        lock = true;
        if(registeredGuilds.size()>0)
            registeredGuilds.clear();
        defaultInteractions = loadInteractions(true);
        //Initialize default processor
        if(defaultInteractions!=null)
            defaultProcessor = new InteractionProcessor(null, defaultInteractions);
        lock = false;
    }

    public void clearGuildInteractionProcessor(Guild guild){
        if(guild!=null){
            registeredGuilds.remove(guild.getId());
        }
    }

    public Tag getTag(String name){
        Object obj = KarrenUtil.instantiate("org.frostbite.karren.Interactions.Tags." + name, Object.class);
        return (Tag)obj;
    }



    public String getCommandPrefix(Guild guild){
        if(guild!=null && Karren.conf.getAllowSQLRW()){
            String prefix = Karren.bot.getSql().getGuild(guild).getCommandPrefix();
            if(prefix!=null && prefix.trim().length()>0)
                return prefix;
        }
        return Karren.conf.getCommandPrefix();
    }

    public InteractionProcessor getInteractionProcessor(Guild guild){
        if(!lock && defaultInteractions.size()>0){
            if(guild!=null){
                if (!registeredGuilds.containsKey(guild.getId())) {
                    ArrayList<Interaction> loadedInteractions = loadInteractions(false);
                    registeredGuilds.put(guild.getId(), new InteractionProcessor(guild, loadedInteractions));
                }
                return registeredGuilds.getOrDefault(guild.getId(), defaultProcessor);
            } else {
                return defaultProcessor;
            }
        }
        return null;
    }

    public Map<String, InteractionProcessor> getRegisteredGuilds() { return registeredGuilds; }
}
