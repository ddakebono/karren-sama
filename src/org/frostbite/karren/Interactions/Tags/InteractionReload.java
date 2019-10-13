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
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

public class InteractionReload extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().isFromGuild()) {
            if (interaction.hasParameter()) {
                if (interaction.getParameter().trim().equalsIgnoreCase("full")) {
                    Karren.log.info("Interaction system reload triggered by " + result.getEvent().getAuthor().getName());
                    Karren.bot.getGuildManager().loadDefaultInteractions();
                    return msg;
                }
            }
            Karren.log.info("Guild " + result.getEvent().getGuild().getName() + " interaction processor has been reloaded by " + result.getEvent().getAuthor().getName());
            Karren.bot.getGuildManager().clearGuildInteractionProcessor(result.getEvent().getGuild());
            return interaction.getRandomTemplate("single").getTemplate();
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "reloadint";
    }

}
