/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import org.frostbite.karren.Interactions.InteractionProcessor;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Karren;

import java.util.Objects;
import java.util.function.Consumer;

public class InteractionCommand implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent event) {
        if(Karren.conf.getEnableInteractions()){
            Guild guild = event.getGuild().block();
            InteractionProcessor ip = Karren.bot.getGuildManager().getInteractionProcessor(guild);
            if(ip!=null && event.getMember().isPresent()){
                if(!Karren.conf.getAllowSQLRW() || !Karren.bot.getSql().getGuildUser(guild, event.getMember().get()).isIgnoreCommands()) {
                    InteractionResult result = ip.run(event);
                    if (result != null){
                        if (result.getOverrideChannel() == null) {
                            if (result.isPrivateMessage()) {
                                Objects.requireNonNull(event.getMember().get().getPrivateChannel().block()).createMessage(result.getMessage()).block();
                            } else {
                                Objects.requireNonNull(event.getMessage().getChannel().block()).createMessage(result.getMessage()).block();
                            }
                        }
                    }
                }
            }
        }
    }
}
