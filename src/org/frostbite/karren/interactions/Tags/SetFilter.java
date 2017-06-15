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

import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

public class SetFilter implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(interaction.getMentionedUsers().size()>0){
            IUser user = interaction.getMentionedUsers().get(0);
            if(!user.equals(event.getAuthor())){
                DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(event.getGuild(), user);
                if(dbGuildUser.isIgnoreCommands()){
                    dbGuildUser.setIgnoreCommands(false);
                    msg = msg.replace("%setting", "disabled");
                } else {
                    dbGuildUser.setIgnoreCommands(true);
                    msg = msg.replace("%setting", "enabled");
                }
                msg = msg.replace("%user", user.getName());
                dbGuildUser.update();
            } else {
                msg = interaction.getRandomTemplatesFail();
            }
        }
        return msg;
    }
}
