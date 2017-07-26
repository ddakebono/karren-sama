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

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.List;

public class RollTopGuilds extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        List<Object[]> guildRanks = Karren.bot.getSql().getGuildRollsTop();
        StringBuilder output = new StringBuilder();
        for(int i=0; i<guildRanks.size(); i++){
            output.append(i+1).append(": ").append(Karren.bot.getClient().getGuildByID(Long.valueOf((String) guildRanks.get(i)[0])).getName()).append(" with ").append(guildRanks.get(i)[1]).append(" total rolls\n");
        }
        msg = msg.replace("%ranks", output.toString());
        return msg;
    }

    @Override
    public String getTagName() {
        return "rolltopguilds";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
