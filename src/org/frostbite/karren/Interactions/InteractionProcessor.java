/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.MessageCreateSpec;
import org.frostbite.karren.Karren;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InteractionProcessor {

    private Guild guild;
    private ArrayList<Interaction> interactions;
    private ArrayList<Interaction> defaultInteractions = null;

    public InteractionProcessor(Guild guild, ArrayList<Interaction> defaultInteractions){
        this.guild = guild;
        this.defaultInteractions = defaultInteractions;
        loadAndUpdateDatabase();
    }

    public void loadAndUpdateDatabase(){
        interactions = new ArrayList<>();
        interactions.addAll(defaultInteractions);
        if(guild!=null) {
            //TODO add audio stuff
            /*if(Karren.bot.getGuildMusicManager(guild) == null || !(guild.getAudioManager().getAudioProvider() instanceof AudioProvider)){
                Karren.log.info("Looks like the GuildMusicManager failed to start, let's try again.");
                try {
                    GuildMusicManager gm = Karren.bot.createGuildMusicManager(guild);
                    guild.getAudioManager().setAudioProvider(gm.getAudioProvider());
                    Karren.log.info("GuildMusicManager initialized on second try for guild " + guild.getName());
                } catch (NullPointerException e){
                    Karren.log.error("Uh oh, looks like we couldn't start the music manager on the second try! Guild " + guild.getName() + " doesn't have a music manager!");
                }
            }*/
            Karren.log.info("Interaction Processor for " + guild.getName() + " ready!");

        } else {
            Karren.log.info("Default interaction processor initialized!");
        }
    }

    public InteractionResult run(MessageCreateEvent event){
        List<Interaction> matches = getAllInteractionMatches(event);
        InteractionResult result = null;
        if(matches.size()>0){
            for(Interaction match : matches){
                MessageCreateSpec message = new MessageCreateSpec();
                result = new InteractionResult(message, event, false, null);
                preloadTags(match);
                processTags(match, result);
            }
        }
        return result;
    }

    private void preloadTags(Interaction interaction){
        for(String tag : interaction.getTags()){
            if(!(tag.equalsIgnoreCase("nodisplay") || tag.equalsIgnoreCase("feelinglucky") || tag.equalsIgnoreCase("prefixed") || tag.equalsIgnoreCase("bot")))
                interaction.getTagCache().add(Karren.bot.getGuildManager().getTag(tag));
        }
    }

    private List<Interaction> getAllInteractionMatches(MessageCreateEvent event){
        List<Interaction> matches = new LinkedList<>();
        for(Interaction check : interactions){
            if(check.checkTriggers(event)) {
                matches.add(check);
                if(!check.isSpecialInteraction())
                    break;
                if(check.interactionUsed())
                    interactions.remove(check);
            }

        }
        return matches;
    }

    public void processTags(Interaction interaction, InteractionResult result){
        String messageStr = interaction.getInitialTemplate(result.getEvent());
        for(Tag tag : interaction.getTagCache()){
            messageStr = tag.handleTemplate(messageStr, interaction, result);
        }
        result.getMessage().setContent(messageStr);
    }
}
