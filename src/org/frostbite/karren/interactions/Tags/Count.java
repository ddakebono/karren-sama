/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags;

import org.frostbite.karren.Database.Objects.DbWordcount;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.EnumSet;

public class Count extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        DbWordcount count = Karren.bot.getSql().getWordCount(interaction.getIdentifier());
        if(count!=null) {
            count.incrementCount();
            count.update();
            Timestamp time = count.getCountStarted();
            return msg.replace("%count", String.valueOf(count.getCount())).replace("%since", time.toString());
        } else {
            return msg;
        }
    }

    @Override
    public String getTagName() {
        return "count";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
