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

import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.HashMap;

public class Return extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        HashMap<IUser, Boolean> departedUsers = ((Depart)Karren.bot.getGuildManager().getTag("depart")).departedUsers;
        if(departedUsers.getOrDefault(event.getMessage().getAuthor(), true) || !interaction.isSpecialInteraction()) {
            DbUser user = Karren.bot.getSql().getUserData(event.getMessage().getAuthor());
            if(departedUsers.putIfAbsent(event.getMessage().getAuthor(), false) != null)
                departedUsers.put(event.getMessage().getAuthor(), false);
            if (user.getTimeLeft()!=null) {
                msg = msg.replace("%away", KarrenUtil.calcAway(user.getTimeLeft().getTime()));
                user.setTimeLeft(null);
                user.update();
                return msg;
            } else {
                if (interaction.isSpecialInteraction())
                    return null;
                else
                    return interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            if(event.getMessage().getMentions().size()>0) {
                boolean isDeparted = departedUsers.getOrDefault(event.getMessage().getMentions().get(0), false);
                if (interaction.isSpecialInteraction() && isDeparted) {
                    DbUser mention = Karren.bot.getSql().getUserData(event.getMessage().getMentions().get(0));
                    msg = interaction.getRandomTemplate("fail").getTemplate();
                    msg = msg.replace("%name", event.getMessage().getAuthor().getName());
                    msg = msg.replace("%mention", event.getMessage().getMentions().get(0).getName());
                    msg = msg.replace("%away", KarrenUtil.calcAway(mention.getTimeLeft().getTime()));
                    return msg;
                }
            }
        }
        return null;
    }

    @Override
    public String getTagName() {
        return "return";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
