/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;

public class Random extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getMessage().getContent().isPresent()) {
            String[] tempArray = result.getEvent().getMessage().getContent().get().split(":");
            if (tempArray.length == 2) {
                return interaction.replaceMsg(msg, "%result", randomList(tempArray[1]));
            } else {
                return interaction.getRandomTemplate("fail").getTemplate();
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "random";
    }

    private static String randomList(String message){
        String[] choiceSet = message.split(",");
        String choice;
        int random = new java.util.Random().nextInt(choiceSet.length);
        choice = choiceSet[random].trim();
        return choice;
    }
}
