/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Database.Objects.DbWordcount;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.EnumSet;

public class Count5 extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        DbWordcount count = Karren.bot.getSql().getWordCount(interaction.getIdentifier());
        if(count!=null) {
            count.incrementCount();
            count.update();
            if((count.getCount()%5)==0) {
                Timestamp time = count.getCountStarted();
                return interaction.replaceMsg(msg,"%count", String.valueOf(count.getCount())).replace("%since", time.toString());
            } else {
                return null;
            }
        } else {
            return msg;
        }
    }

    @Override
    public String getTagName() {
        return "count5";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
