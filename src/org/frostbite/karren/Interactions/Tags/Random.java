/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class Random extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String[] tempArray = event.getMessage().getContent().split(":");
        if(tempArray.length==2){
            return interaction.replaceMsg(msg,"%result", randomList(tempArray[1]));
        } else {
            return interaction.getRandomTemplate("fail").getTemplate();
        }
    }

    @Override
    public String getTagName() {
        return "random";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }

    private static String randomList(String message){
        String[] choiceSet = message.split(",");
        String choice;
        int random = new java.util.Random().nextInt(choiceSet.length);
        choice = choiceSet[random].trim();
        return choice;
    }
}
