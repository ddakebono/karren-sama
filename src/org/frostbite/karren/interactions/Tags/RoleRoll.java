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
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RoleRoll implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(!event.getMessage().getChannel().isPrivate()) {
            DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(event.getGuild(), event.getAuthor());
            if (dbGuildUser.getRollTimeout() == null || new Timestamp(System.currentTimeMillis()).after(dbGuildUser.getRollTimeout())) {
                java.util.Random rng = new Random();
                int roll = rng.nextInt(100);
                if (roll >= (Karren.bot.getSql().getGuild(event.getGuild()).getRollDifficulty() >= 0 ? Karren.bot.getSql().getGuild(event.getGuild()).getRollDifficulty() : 95)) {
                    List<IRole> rollRoles = new LinkedList<>();
                    for (IRole role : event.getGuild().getRoles())
                        if (role.getName().contains("lotto-"))
                            rollRoles.add(role);
                    IRole rngRole = rollRoles.get(rng.nextInt(rollRoles.size()));
                    event.getAuthor().addRole(rngRole);
                    msg = msg.replace("%rngrole", rngRole.getName());
                    dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 604800000));
                } else {
                    dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 21600000));
                    msg = interaction.getRandomTemplatesFail();
                }
                dbGuildUser.update();
            } else {
                msg = interaction.getRandomTemplatesPermError();

            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM @ HH:mm");
            msg = msg.replace("%timeremaining", dateFormat.format(dbGuildUser.getRollTimeout()));
            return msg;
        }
        return "This cannot be used in a private message!";
    }
}
