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

import org.frostbite.karren.Database.Models.tables.records.UserRecord;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;

import java.sql.Timestamp;
import java.util.*;
import java.util.Random;

public class RoleRoll implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        UserRecord dbUser = Karren.bot.getSql().getUserData(event.getAuthor());
        if(dbUser.getRoletimeout()==null || new Timestamp(System.currentTimeMillis()).after(dbUser.getRoletimeout())) {
            java.util.Random rng = new Random();
            int roll = rng.nextInt(100);
            if (roll >= (Karren.bot.getSql().getGuild(event.getGuild()).getRolldifficulty() != null ? Karren.bot.getSql().getGuild(event.getGuild()).getRolldifficulty() : 95)) {
                List<IRole> rollRoles = new LinkedList<>();
                for (IRole role : event.getGuild().getRoles())
                    if (role.getName().contains("lotto-"))
                        rollRoles.add(role);
                IRole rngRole = rollRoles.get(rng.nextInt(rollRoles.size()));
                event.getAuthor().addRole(rngRole);
                msg = msg.replace("%rngrole", rngRole.getName());
                dbUser.setRoletimeout(new Timestamp(System.currentTimeMillis() + 604800000));
            } else {
                dbUser.setRoletimeout(new Timestamp(System.currentTimeMillis() + 21600000));
                msg = interaction.getRandomTemplatesFail();
            }
            dbUser.update();
        } else {
            msg = interaction.getRandomTemplatesPermError();
        }
        return msg;
    }
}
