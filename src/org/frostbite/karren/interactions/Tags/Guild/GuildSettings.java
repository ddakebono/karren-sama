/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.Guild;

import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class GuildSettings extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        DbGuild guild = Karren.bot.getSql().getGuild(event.getGuild());
        interaction.replaceMsg(msg, "%difficulty", Integer.toString(guild.getRollDifficulty()));
        interaction.replaceMsg(msg, "%maxvolume", Integer.toString(guild.getMaxVolume()));
        interaction.replaceMsg(msg, "%prefix", ("\"" + Karren.bot.getGuildManager().getCommandPrefix(event.getGuild()) + "\""));
        interaction.replaceMsg(msg, "%range", Integer.toString(guild.getRandomRange()));
        return msg;
    }

    @Override
    public String getTagName() {
        return "guildsettings";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
