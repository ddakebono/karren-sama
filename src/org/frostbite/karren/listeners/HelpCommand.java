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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class HelpCommand implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        if(event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help")){
            MessageBuilder helpMsg = new MessageBuilder(bot);
            ArrayList<MessageBuilder> messages = new ArrayList<>();
            String moreInfo = event.getMessage().getContent().replace(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help", "").trim();
            if(moreInfo.length()==0) {
                try {
                    helpMsg.withChannel(event.getAuthor().getOrCreatePMChannel());
                    helpMsg.withContent("To view more information about a specific command enter " + Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help CommandName\n");
                    for (Interaction help : Karren.bot.getGuildManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions()) {
                        if (KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), help.getPermissionLevel())) {
                            String helpData;
                            if (Arrays.asList(help.getTags()).contains("prefixed")) {
                                helpData = "__**" + help.getIdentifier() + "**__ | " + help.getHelptext() + "\n\n";
                            } else {
                                helpData = "__**" + help.getIdentifier() + "**__ | " + help.getHelptext() + "\n\n";
                            }
                            if(helpMsg.getContent().length()+helpData.length()>=1980){
                                messages.add(helpMsg);
                                helpMsg = new MessageBuilder(bot);
                                helpMsg.withChannel(event.getAuthor().getOrCreatePMChannel());
                            } else {
                                helpMsg.appendContent(helpData);
                            }
                        }
                    }
                    messages.add(helpMsg);
                } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
                    e.printStackTrace();
                }
            } else {
                Optional<Interaction> interaction = Karren.bot.getGuildManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions().stream().filter(i -> i.getIdentifier().equalsIgnoreCase(moreInfo)).findFirst();
                if(interaction.isPresent()){
                    Interaction helpInteraction = interaction.get();
                    if(KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), helpInteraction.getPermissionLevel())) {
                        try {
                            helpMsg.withChannel(event.getAuthor().getOrCreatePMChannel());
                            if (Arrays.asList(helpInteraction.getTags()).contains("prefixed"))
                                helpMsg.withContent("__**" + helpInteraction.getIdentifier() + "**__\n**Command**: " + Karren.conf.getCommandPrefix() + helpInteraction.getTriggers()[0] + "\n**Sample Output**: " + helpInteraction.getRandomTemplate("normal").getTemplate() + "\n**Help text**: " + helpInteraction.getHelptext());
                            else
                                helpMsg.withContent("__**" + helpInteraction.getIdentifier() + "**__\n**Triggers**: " + helpInteraction.getActivatorsToString() + "\n**Amount of triggers needed to trigger**: " + helpInteraction.getConfidence() + "\n**Sample Output**: " + helpInteraction.getRandomTemplate("normal").getTemplate() + "\n**Help text**: " + helpInteraction.getHelptext());
                            messages.add(helpMsg);
                        } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Send messages to user
            messages.forEach(MessageBuilder::send);
        }
    }
}

