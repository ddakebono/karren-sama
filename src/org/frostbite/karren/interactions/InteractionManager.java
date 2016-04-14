/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions;


import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Tags.*;
import org.frostbite.karren.listeners.InteractionCommands;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InteractionManager {
    private ArrayList<Interaction> interactions;
    private Map<String, Tag> handlers;

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
        handlers.put("speak", new Speak());
        handlers.put("stopspeak", new StopSpeak());
        handlers.put("topic", new Topic());
        handlers.put("parameter", new Parameter());
        handlers.put("count5", new Count5());
        handlers.put("parent", new Parent());
        handlers.put("child", new Child());
        handlers.put("disableinteraction", new DisableInteraction());
        handlers.put("enableinteraction", new EnableInteraction());
        handlers.put("count", new Count());
    }

    public void loadInteractions(){
        Gson gson = new Gson();
        interactions = new ArrayList<>();
        File intDir = new File("conf/Interactions");
        if(intDir.isDirectory()){
            File[] intFiles = KarrenUtil.getFilesInFolders(intDir);
            for(File file : intFiles){
                try {
                    Interaction tempInteraction = gson.fromJson(new FileReader(file), Interaction.class);
                    tempInteraction.setIdentifier(FilenameUtils.removeExtension(file.getName()));
                    interactions.add(tempInteraction);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Karren.log.info("No Interaction detected, interaction system unregistered.");
            Karren.bot.getClient().getDispatcher().unregisterListener(new InteractionCommands());
        }
    }

    public MessageBuilder handle(MessageReceivedEvent event) throws HTTP429Exception, DiscordException {
        String returned;
        MessageBuilder result = null;
        for(Interaction check : interactions){
            returned = check.handleMessage(event);
            if(returned!=null){
                Karren.log.debug("Interaction match for " + check.getIdentifier() + ", handling templates!");
                result = new MessageBuilder(Karren.bot.getClient()).withChannel(event.getMessage().getChannel());
                for(String tag : check.getTags()){
                    if(!tag.equalsIgnoreCase("pm")) {
                        Tag handler = handlers.get(tag.toLowerCase());
                        if (handler != null && returned!=null)
                            returned = handler.handleTemplate(returned, check, result, event);
                        else if(!tag.equalsIgnoreCase("bot") && !tag.equalsIgnoreCase("prefixed") && !tag.equalsIgnoreCase("special") && returned!=null)
                            Karren.log.error("Please check interaction " + check.getIdentifier() + " as the file contains invalid tags!");
                    }
                }
                if(returned!=null)
                    result.withContent(returned);
                else
                    result = null;
                if(!check.isSpecialInteraction())
                    break;
            }
        }
        return result;
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

    public ArrayList<Interaction> getInteractions(){return interactions;}

}
