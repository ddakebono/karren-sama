/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class InteractionReload extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.hasParameter()){
            if(interaction.getParameter().trim().equalsIgnoreCase("full")){
                Karren.log.info("Interaction system reload triggered by " + event.getMessage().getAuthor().getName());
                Karren.bot.getGuildManager().loadDefaultInteractions();
                return msg;
            }
        }
        Karren.log.info("Guild " + event.getGuild().getName() + " interaction processor has been reloaded by " + event.getAuthor().getName());
        Karren.bot.getGuildManager().clearGuildInteractionProcessor(event.getGuild());
        return interaction.getRandomTemplate("single").getTemplate();
    }

    @Override
    public String getTagName() {
        return "reloadint";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
