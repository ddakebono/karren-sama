/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionProcessor;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelpListener extends ListenerAdapter {

    ArrayList<Message> messageQueuePrefixed = new ArrayList<>();
    ArrayList<Message> messageQueue = new ArrayList<>();
    EmbedBuilder currentEmbedPrefixed;
    EmbedBuilder currentEmbed;
    int page = 1;

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        //Got help command from private message
        cleanup();
        String message = event.getMessage().getContentDisplay();
        if(event.getMessage().getContentDisplay().startsWith(Karren.bot.getGuildManager().getCommandPrefix(null) + "help")) {
            message = message.replaceFirst(Karren.bot.getGuildManager().getCommandPrefix(null) + "help", "").trim();
            Karren.log.info("Help command triggered by " + event.getAuthor().getName());
            InteractionProcessor ip = Karren.bot.getGuildManager().getInteractionProcessor(null);
            if(message.isEmpty()) {
                for (Interaction interaction : ip.getInteractions()) {
                    if (interaction.getPermissionLevel() != null && !interaction.getPermissionLevel().isEmpty() && !event.getAuthor().getId().equals(Karren.conf.getOperatorDiscordID())) {
                        continue;
                    }

                    if (interaction.getTagsToString().contains("prefixed"))
                        currentEmbedPrefixed = addCommandToEmbedPrefixed(currentEmbedPrefixed, interaction, null);
                    else
                        currentEmbed = addCommandToEmbed(currentEmbed, interaction);
                }

                //Finalize message queue
                if (currentEmbedPrefixed != null) {
                    finalizeEmbed("Help - Page " + page, "Command help for interactions using prefixed mode", currentEmbedPrefixed, messageQueuePrefixed);
                    page++;
                }
                if (currentEmbed != null) {
                    finalizeEmbed("Help - Page " + page, "Command help for interactions using non prefixed mode", currentEmbed, messageQueue);
                    page++;
                }

                currentEmbed = null;
                currentEmbedPrefixed = null;

                sendMessages(messageQueuePrefixed, event.getAuthor().openPrivateChannel().complete());
                sendMessages(messageQueue, event.getAuthor().openPrivateChannel().complete());
            } else {
                String finalMessage = message;
                Optional<Interaction> interactionOpt = ip.getInteractions().stream().filter(x -> x.getIdentifier().equalsIgnoreCase(finalMessage)).findFirst();
                if(interactionOpt.isPresent()){
                    Interaction interaction = interactionOpt.get();
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Help - " + interaction.getIdentifier());
                    builder.setDescription("Help info for command " + interaction.getIdentifier());
                    builder.addField("Help", interaction.getHelptext(), false);
                    builder.addField("Template Sample", interaction.getRandomTemplate("normal").getTemplate(), false);
                    if(interaction.getTriggers().length>0) {
                        if(interaction.getTagsToString().contains("prefixed"))
                            builder.addField("Command", Karren.bot.getGuildManager().getCommandPrefix(null) + interaction.getTriggers()[0], true);
                        else
                            builder.addField("Command", interaction.getTriggers()[0], true);
                    }
                    if(interaction.getPermissionLevel()!=null && interaction.getPermissionLevel().length()>0)
                        builder.addField("Permission Level", interaction.getPermissionLevel(), true);
                    else
                        builder.addField("Permission Level", "None", true);
                    builder.addField("Confidence", Integer.toString(interaction.getConfidence()), true);
                    event.getAuthor().openPrivateChannel().complete().sendMessage(builder.build()).queue();
                }
            }
        }
    }

    private void cleanup(){
        messageQueuePrefixed.clear();
        messageQueue.clear();
        page = 1;
        currentEmbed = null;
        currentEmbedPrefixed = null;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        //Got help command from guild
        cleanup();
        String message = event.getMessage().getContentDisplay();
        if(message.startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help")) {
            message = message.replaceFirst(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "help", "").trim();
            Karren.log.info("Help command triggered by " + event.getAuthor().getName());
            InteractionProcessor ip = Karren.bot.getGuildManager().getInteractionProcessor(event.getGuild());
            if(message.isEmpty()) {
                for (Interaction interaction : ip.getInteractions()) {
                    if (interaction.getPermissionLevel() != null && interaction.getPermissionLevel().isEmpty() && !event.getAuthor().getId().equals(Karren.conf.getOperatorDiscordID())) {
                        List<Role> rolesMatch = Objects.requireNonNull(event.getMember()).getRoles().stream().filter(x -> x.getName().equalsIgnoreCase(interaction.getPermissionLevel())).collect(Collectors.toList());
                        if (rolesMatch.size() == 0)
                            continue;
                    }

                    if (interaction.getTagsToString().contains("prefixed"))
                        currentEmbedPrefixed = addCommandToEmbedPrefixed(currentEmbedPrefixed, interaction, event.getGuild());
                    else
                        currentEmbed = addCommandToEmbed(currentEmbed, interaction);
                }

                //Finalize message queue
                if (currentEmbedPrefixed != null) {
                    finalizeEmbed("Help - Page " + page, "Command help for interactions using prefixed mode", currentEmbedPrefixed, messageQueuePrefixed);
                    page++;
                }
                if (currentEmbed != null) {
                    finalizeEmbed("Help - Page " + page, "Command help for interactions using non prefixed mode", currentEmbed, messageQueue);
                    page++;
                }

                currentEmbed = null;
                currentEmbedPrefixed = null;

                sendMessages(messageQueuePrefixed, event.getAuthor().openPrivateChannel().complete());
                sendMessages(messageQueue, event.getAuthor().openPrivateChannel().complete());
            } else {
                String finalMessage = message;
                Optional<Interaction> interactionOpt = ip.getInteractions().stream().filter(x -> x.getIdentifier().equalsIgnoreCase(finalMessage)).findFirst();
                if(interactionOpt.isPresent()){
                    Interaction interaction = interactionOpt.get();
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Help - " + interaction.getIdentifier());
                    builder.setDescription("Help info for command " + interaction.getIdentifier());
                    builder.addField("Help", interaction.getHelptext(), false);
                    builder.addField("Template Sample", interaction.getRandomTemplate("normal").getTemplate(), false);
                    if(interaction.getTriggers().length>0) {
                        if(interaction.getTagsToString().contains("prefixed"))
                            builder.addField("Command", Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + interaction.getTriggers()[0], true);
                        else
                            builder.addField("Command", interaction.getTriggers()[0], true);
                    }
                    if(interaction.getPermissionLevel()!=null && interaction.getPermissionLevel().length()>0)
                        builder.addField("Permission Level", interaction.getPermissionLevel(), true);
                    else
                        builder.addField("Permission Level", "None", true);
                    builder.addField("Confidence", Integer.toString(interaction.getConfidence()), true);
                    event.getAuthor().openPrivateChannel().complete().sendMessage(builder.build()).queue();
                }
            }
        }
    }

    private EmbedBuilder addCommandToEmbed(EmbedBuilder currentEmbed, Interaction interaction){
        if(currentEmbed!=null && currentEmbed.getFields().size()>24){
            //Finalize embed
            finalizeEmbed("Help - Page " + page, "Command help for interactions using non prefixed mode", currentEmbed, messageQueue);
            page++;
            currentEmbed = null;
        }

        if(currentEmbed==null)
            currentEmbed = new EmbedBuilder();

        currentEmbed.addField(
                interaction.getFriendlyName() != null ? interaction.getFriendlyName() : interaction.getIdentifier(),
                String.format("%s", interaction.getHelptext()),
                false
        );

        return currentEmbed;
    }

    private void finalizeEmbed(String title, String desc, EmbedBuilder embed, ArrayList<Message> queue){
        Karren.log.info("Embed Stats: " + embed.getFields().size());
        embed.setTitle(title);
        embed.setDescription(desc);
        addEmbedToNewMessage(embed, queue);
    }

    private EmbedBuilder addCommandToEmbedPrefixed(EmbedBuilder currentEmbed, Interaction interaction, Guild guild){
        if(currentEmbed!=null && currentEmbed.getFields().size()>24){
            //Finalize embed
            finalizeEmbed("Help - Page " + page, "Command help for interactions using prefixed mode", currentEmbed, messageQueue);
            page++;
            currentEmbed = null;
        }

        if(currentEmbed==null)
            currentEmbed = new EmbedBuilder();

        String prefix = ".";

        if(guild!=null){
            prefix = Karren.bot.getGuildManager().getCommandPrefix(guild);
        }

        currentEmbed.addField(
                interaction.getIdentifier(),
                String.format("%s | Command: `%s%s`", interaction.getHelptext(), prefix, interaction.getTriggers().length>0 ? interaction.getTriggers()[0] : "(no triggers)"),
                false
        );

        return currentEmbed;
    }

    private void addEmbedToNewMessage(EmbedBuilder embed, ArrayList<Message> messageQueue){
        MessageBuilder message = new MessageBuilder();
        message.setEmbed(embed.build());
        messageQueue.add(message.build());
    }

    private void sendMessages(ArrayList<Message> messageQueue, MessageChannel channel){
        Karren.log.info("MessageQueue " + messageQueue.size());

        for(Message message : messageQueue){
            channel.sendMessage(message).queue();
        }
    }

}
