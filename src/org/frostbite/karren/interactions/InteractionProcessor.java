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
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.MessageBuilder;

import java.util.ArrayList;

public class InteractionProcessor {

    private ArrayList<Interaction> interactions;
    private ArrayList<Interaction> defaultInteractions = null;
    private IGuild guild; //CAN BE NULL

    public InteractionProcessor(IGuild guild){
        this(guild, null);
    }

    public InteractionProcessor(IGuild guild, ArrayList<Interaction> defaultInteractions){
        this.guild = guild;
        this.defaultInteractions = defaultInteractions;
        loadAndUpdateDatabase();
    }

    public void loadAndUpdateDatabase(){
        interactions = new ArrayList<>();
        interactions.addAll(defaultInteractions);
        Karren.log.info("Default interaction processor initialized!");
    }

    public MessageBuilder handle(MessageReceivedEvent event) {
        String returned;
        MessageBuilder result = null;
        for(Interaction check : interactions){
            returned = check.handleMessage(event);
            if(returned!=null){
                Karren.log.debug("Interaction match for " + check.getIdentifier() + ", handling templates!");
                result = new MessageBuilder(Karren.bot.getClient()).withChannel(event.getMessage().getChannel());
                if(!check.isPermBad()) {
                    for (String tag : check.getTags()) {
                        if (!tag.equalsIgnoreCase("pm")) {
                            Tag handler = Karren.bot.getInteractionManager().getHandlers().get(tag.toLowerCase());
                            if (handler != null && returned != null)
                                returned = handler.handleTemplate(returned, check, result, event);
                            else if (!tag.equalsIgnoreCase("bot") && !tag.equalsIgnoreCase("prefixed") && !tag.equalsIgnoreCase("special") && returned != null)
                                Karren.log.error("Please check interaction " + check.getIdentifier() + " as the file contains invalid tags!");
                        }
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

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }
}
