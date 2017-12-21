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
import org.pircbotx.hooks.events.MessageEvent;

public class InteractionReload extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageEvent event) {
        if(interaction.hasParameter()){
            if(interaction.getParameter().trim().equalsIgnoreCase("full")){
                Karren.log.info("Interaction system reload triggered by " + event.getUser().getNick());
                Karren.bot.getGuildManager().loadDefaultInteractions();
                return msg;
            }
        }
        Karren.log.info("Guild " + event.getChannel().getName() + " interaction processor has been reloaded by " + event.getUser().getNick());
        Karren.bot.getGuildManager().clearGuildInteractionProcessor(event.getChannel());
        return interaction.getRandomTemplate("single").getTemplate();
    }

    @Override
    public String getTagName() {
        return "reloadint";
    }

}
