/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class OverrideChannel extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        response.withChannel(Karren.bot.getClient().getChannelByID(Long.getLong(interaction.getChannel())));
        return msg;
    }

    @Override
    public String getTagName() {
        return "overridechanel";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.READ_MESSAGES);
    }
}
