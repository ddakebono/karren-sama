/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions;

import org.frostbite.karren.Karren;
import org.pircbotx.Channel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;

public class InteractionProcessor {

    private ArrayList<Interaction> interactions;
    private ArrayList<Interaction> defaultInteractions = null;
    private Channel guild; //CAN BE NULL

    public InteractionProcessor(Channel guild){
        this(guild, null);
    }

    public InteractionProcessor(Channel guild, ArrayList<Interaction> defaultInteractions){
        this.guild = guild;
        this.defaultInteractions = defaultInteractions;
        loadAndUpdateDatabase();
    }

    public void loadAndUpdateDatabase(){
        interactions = new ArrayList<>();
        interactions.addAll(defaultInteractions);
        if(guild!=null) {
            Karren.log.info("Interaction Processor for " + guild.getName() + " ready!");

        } else {
            Karren.log.info("Default interaction processor initialized!");
        }
    }

    public String handle(MessageEvent event) {
        String returned = null;
        for(Interaction check : interactions){
            returned = check.handleMessage(event);
            if(returned!=null){
                check.setLock(true);
                if(!check.getTagsToString().contains("nodisplay"))
                    Karren.log.debug("Interaction match for " + check.getIdentifier() + ", handling templates! (Confidence: " + check.getConfidenceChecked() + ")");
                if(!check.isPermBad()) {
                    for (String tag : check.getTags()) {
                        if (!tag.equalsIgnoreCase("pm") && !check.isStopProcessing()) {
                            Tag handler = Karren.bot.getGuildManager().getTag(tag.toLowerCase());
                            if (handler != null && returned != null)
                                returned = handler.handleTemplate(returned, check, event);
                            else if (!tag.equalsIgnoreCase("bot") && !tag.equalsIgnoreCase("prefixed") && !tag.equalsIgnoreCase("special") && !tag.equalsIgnoreCase("feelinglucky") && !tag.equalsIgnoreCase("nodisplay") && returned != null)
                                Karren.log.error("Please check interaction " + check.getIdentifier() + " as the file contains invalid tags!");
                        }
                    }
                    if(check.interactionUsed())
                        Karren.bot.getGuildManager().getInteractionProcessor(event.getChannel()).getInteractions().remove(check);
                }
                check.setLock(false);
                if(returned!=null && returned.length()>0){
                    returned = returned.replace("%prefix", Karren.bot.getGuildManager().getCommandPrefix(event.getChannel()));
                }

                if(!check.isSpecialInteraction())
                    break;

            }
        }
        return returned;
    }

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }
}
