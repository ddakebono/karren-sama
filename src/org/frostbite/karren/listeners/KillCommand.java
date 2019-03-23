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
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.frostbite.karren.Karren;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class KillCommand implements Consumer<MessageCreateEvent> {

    @Override
	public void accept(MessageCreateEvent event){
        User bot = Karren.bot.client.getSelf().block();
        Optional<User> authorOpt = event.getMessage().getAuthor();
        if(event.getMember().isPresent()) {
            assert bot != null;
            if (bot.getId().equals(event.getMember().get().getId()))
                return;
        }
        if(event.getMessage().getContent().isPresent() && authorOpt.isPresent()) {
            Karren.log.info("New Message: \"" + event.getMessage().getContent().get() + "\" From user: " + authorOpt.get().getUsername() + " in Channel: " + Objects.requireNonNull(event.getMessage().getChannel().block()).getId().asString());
            if (event.getMessage().getContent().get().startsWith(Karren.bot.getGuildManager().getCommandPrefix(event.getGuild().block()) + "kill")) {
                Optional<Member> optMember = event.getMember();
                if (optMember.isPresent()) {
                    if (optMember.get().getId().asString().equals(Karren.conf.getOperatorDiscordID())) {
                        Karren.bot.killBot(optMember.get().getNickname().isPresent() ? optMember.get().getNickname().get() : optMember.get().getDisplayName());
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block()).createMessage("Hold on a sec, this command can only be used by my operator.");
                    }
                }
            }
        }
	}
}
