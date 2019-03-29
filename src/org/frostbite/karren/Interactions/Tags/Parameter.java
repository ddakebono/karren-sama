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
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.Arrays;
import java.util.EnumSet;

public class Parameter extends Tag {

    //Currently the parameter tag only works with Prefixed Interactions, use on non prefixed Interactions isn't possible yet.

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        String message = event.getMessage().getContent();
        if(interaction.getTriggers()!=null) {
            for (String trigger : interaction.getTriggers()) {
                if (event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + trigger)) {
                    message = message.replace(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + trigger, "").trim();
                    break;
                }
                //Patch for prefixed Interactions
                if (event.getMessage().getContent().toLowerCase().startsWith(trigger) && !Arrays.asList(interaction.getTags()).contains("prefixed")){
                    message = message.toLowerCase().replace(trigger, "").trim();
                    break;
                }
            }
        }

        interaction.setParameter(message);
        return msg;
    }

    @Override
    public String getTagName() {
        return "parameter";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
