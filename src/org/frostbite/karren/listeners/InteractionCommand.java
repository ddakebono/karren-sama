/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.frostbite.karren.Interactions.InteractionProcessor;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InteractionCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            if (Karren.conf.getEnableInteractions()) {
                Guild guild = null;
                if (event.isFromGuild())
                    guild = event.getGuild();
                InteractionProcessor ip = Karren.bot.getGuildManager().getInteractionProcessor(guild);
                if (ip != null) {
                    if (!Karren.bot.getSql().getGuildUser(guild, event.getAuthor()).isIgnoreCommands()) {
                        InteractionResult result = ip.run(event);
                        if (result != null) {
                            MessageBuilder msgBuilder = new MessageBuilder();
                            msgBuilder.setContent(result.getMessage());
                            if (result.getEmbed() != null)
                                msgBuilder.setEmbed(result.getEmbed());
                            if (!result.isDoNotSend() && !msgBuilder.isEmpty()) {
                                MessageAction sendAction = null;
                                if (result.isPrivateMessage()) {
                                    sendAction = event.getAuthor().openPrivateChannel().complete().sendMessage(msgBuilder.build());
                                } else {
                                    if (result.getOverrideChannel() == null) {
                                        sendAction = event.getChannel().sendMessage(msgBuilder.build());
                                    } else {
                                        TextChannel channel = (TextChannel) Objects.requireNonNull(guild).getGuildChannelById(ChannelType.TEXT, result.getOverrideChannel());
                                        if (channel != null) {
                                            sendAction = channel.sendMessage(msgBuilder.build());
                                        }
                                    }
                                }
                                if (sendAction != null)
                                    sendAction.queue();
                            }
                        }
                    }
                }
            }
        }
    }
}
