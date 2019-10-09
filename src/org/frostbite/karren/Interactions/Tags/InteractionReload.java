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

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;

import java.util.Optional;

public class InteractionReload extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();
        Optional<User> authorOpt = result.getEvent().getMessage().getAuthor();
        if(authorOpt.isPresent() && guild!=null) {
            User author = authorOpt.get();
            if (interaction.hasParameter()) {
                if (interaction.getParameter().trim().equalsIgnoreCase("full")) {
                    Karren.log.info("Interaction system reload triggered by " + author.getUsername());
                    Karren.bot.getGuildManager().loadDefaultInteractions();
                    return msg;
                }
            }
            Karren.log.info("Guild " + guild.getName() + " interaction processor has been reloaded by " + author.getUsername());
            Karren.bot.getGuildManager().clearGuildInteractionProcessor(guild);
            return interaction.getRandomTemplate("single").getTemplate();
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "reloadint";
    }

}
