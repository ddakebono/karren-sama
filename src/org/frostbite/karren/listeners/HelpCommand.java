/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.Karren;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.*;

import java.util.Arrays;
import java.util.Optional;

public class HelpCommand implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        if(event.getMessage().getContent().startsWith(Karren.conf.getCommandPrefix() + "help")){
            MessageBuilder helpMsg = new MessageBuilder(bot);
            String moreInfo = event.getMessage().getContent().replace(Karren.conf.getCommandPrefix() + "help", "").trim();
            if(moreInfo.length()==0) {
                MessageBuilder prefixedHelpMsg = new MessageBuilder(bot);
                try {
                    helpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                    prefixedHelpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                    helpMsg.withContent("To view more information about a specific command enter " + Karren.conf.getCommandPrefix() + "help CommandName\nAll commands using sentence based triggers.\n");
                    prefixedHelpMsg.withContent("All commands using the " + Karren.conf.getCommandPrefix() + " prefix.\n");
                    for (Interaction help : Karren.bot.getInteractionManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions()) {
                        if (KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getClient(), help.getPermissionLevel())) {
                            if (Arrays.asList(help.getTags()).contains("prefixed")) {
                                prefixedHelpMsg.appendContent("__**" + help.getIdentifier() + "**__ | " + help.getHelptext() + "\n\n");
                            } else {
                                helpMsg.appendContent("__**" + help.getIdentifier() + "**__ | " + help.getHelptext() + "\n\n");
                            }
                        }
                    }
                    helpMsg.send();
                    prefixedHelpMsg.send();

                } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
                    e.printStackTrace();
                }
            } else {
                Optional<Interaction> interaction = Karren.bot.getInteractionManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions().stream().filter(i -> i.getIdentifier().equalsIgnoreCase(moreInfo)).findFirst();
                if(interaction.isPresent()){
                    Interaction helpInteraction = interaction.get();
                    if(KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getClient(), helpInteraction.getPermissionLevel())) {
                        try {
                            helpMsg.withChannel(event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor()));
                            if (Arrays.asList(helpInteraction.getTags()).contains("prefixed"))
                                helpMsg.withContent("__**" + helpInteraction.getIdentifier() + "**__\n**Command**: " + Karren.conf.getCommandPrefix() + helpInteraction.getTriggers()[0] + "\n**Sample Output**: " + helpInteraction.getRandomTemplates() + "\n**Help text**: " + helpInteraction.getHelptext());
                            else
                                helpMsg.withContent("__**" + helpInteraction.getIdentifier() + "**__\n**Triggers**: " + helpInteraction.getActivatorsToString() + "\n**Amount of triggers needed to trigger**: " + helpInteraction.getConfidence() + "\n**Sample Output**: " + helpInteraction.getRandomTemplates() + "\n**Help text**: " + helpInteraction.getHelptext());
                            helpMsg.send();
                        } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

