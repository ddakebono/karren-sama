/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class HelpCommand implements IListener<MessageReceivedEvent> {
    public void handle(MessageReceivedEvent event){
        IDiscordClient bot = event.getClient();
        if(event.getMessage().getContent().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help")){
            ArrayList<MessageBuilder> messages = new ArrayList<>();
            String moreInfo = event.getMessage().getContent().replace(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help", "").trim();
            if(moreInfo.length()==0) {
                try {
                    ArrayList<EmbedObject> prefixedEmbed = new ArrayList<>();
                    ArrayList<EmbedObject> confidenceEmbed = new ArrayList<>();
                    EmbedBuilder prefixed = new EmbedBuilder();
                    EmbedBuilder confidence = new EmbedBuilder();
                    for (Interaction help : Karren.bot.getGuildManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions()) {
                        if (KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), help.getPermissionLevel()) && !help.getTagsToString().contains("nodisplay")) {
                            if (Arrays.asList(help.getTags()).contains("prefixed")) {
                                if(prefixed.getFieldCount()>=25) {
                                    prefixedEmbed.add(prefixed.build());
                                    prefixed = new EmbedBuilder();
                                }
                                if(prefixed.getFieldCount()==0){
                                    prefixed.withTitle("**__Prefixed Commands__**");
                                    prefixed.withColor(Color.RED);
                                    prefixed.withDescription("All commands listed here use the server prefix \"" + Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "\"");
                                }
                                prefixed.appendField((help.getFriendlyName()!=null&&help.getFriendlyName().length()>0)?help.getFriendlyName():help.getIdentifier(), help.getHelptext(), false);
                            } else {
                                if(confidence.getFieldCount()>=25) {
                                    confidenceEmbed.add(confidence.build());
                                    confidence = new EmbedBuilder();
                                }
                                if(confidence.getFieldCount()==0){
                                    confidence.withTitle("**__Non-prefixed Commands__**");
                                    confidence.withColor(Color.RED);
                                    confidence.withDescription("All commands listed here are triggered by using words in a normal sentence, meet the threshold and it fires.");
                                }
                                confidence.appendField((help.getFriendlyName()!=null&&help.getFriendlyName().length()>0)?help.getFriendlyName():help.getIdentifier(), help.getHelptext(), false);
                            }
                        }
                    }
                    prefixedEmbed.add(prefixed.build());
                    confidenceEmbed.add(confidence.build());
                    for(EmbedObject embed : prefixedEmbed){
                        messages.add(new MessageBuilder(bot).withChannel(event.getAuthor().getOrCreatePMChannel()).withEmbed(embed));
                    }
                    for(EmbedObject embed : confidenceEmbed)
                        messages.add(new MessageBuilder(bot).withChannel(event.getAuthor().getOrCreatePMChannel()).withEmbed(embed));
                } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
                    e.printStackTrace();
                }
            } else {
                Optional<Interaction> interaction = Karren.bot.getGuildManager().getInteractionProcessor(event.getMessage().getGuild()).getInteractions().stream().filter(i -> i.getIdentifier().equalsIgnoreCase(moreInfo)).findFirst();
                if(interaction.isPresent()){
                    Interaction helpInteraction = interaction.get();
                    if(KarrenUtil.hasRole(event.getMessage().getAuthor(), event.getGuild(), helpInteraction.getPermissionLevel())) {
                        try {
                            MessageBuilder helpMsg = new MessageBuilder(bot);
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

