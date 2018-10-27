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

public class InteractionProcessor {

    private Guild guild;

    public MessageCreateSpec run(MessageCreateEvent event){
        Interaction match = checkForMatches(event);
        if(match != null) {
            //Matching interaction found
            preloadAllTags(match.getTags(), match);
            return processTags(match);
        }
        return null;
    }

    public Interaction checkForMatches(MessageCreateEvent event){
        return null;
    }

    public void preloadAllTags(String[] tags, Interaction interaction){

    }

    public MessageCreateSpec processTags(Interaction interaction){
        return null;
    }
}
