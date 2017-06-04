/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.InstantReplay;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class StartListening implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(!Karren.bot.getIrm().isGuildIRActive(event.getGuild()) && event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel()!=null){
            Karren.bot.getIrm().getInstantReplay(event.getGuild(), event.getAuthor()).startListening();
        }
        return msg;
    }
}
