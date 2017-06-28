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
            List<IRole> rollRoles = new LinkedList<>();
            if(!event.getMessage().getGuild().getRolesForUser(event.getClient().getOurUser()).contains("MANAGE_ROLES"))
                return interaction.getRandomTemplate("noroleperm").getTemplate();
            for (IRole role : event.getGuild().getRoles())
                if (role.getName().contains("lotto-"))
                    rollRoles.add(role);
            if(rollRoles.size()>0) {
                if (dbGuildUser.getRollTimeout() == null || new Timestamp(System.currentTimeMillis()).after(dbGuildUser.getRollTimeout())) {
                    dbGuildUser.incrementTotalRolls();
                    java.util.Random rng = new Random();
                    int roll = rng.nextInt(100);
                    int bonus = (dbGuildUser.getRollsSinceLastClear() / 2);
                    int dc = (Karren.bot.getSql().getGuild(event.getGuild()).getRollDifficulty() >= 0 ? Karren.bot.getSql().getGuild(event.getGuild()).getRollDifficulty() : 95);
                    Karren.log.info("Rolled " + roll + " against a DC of " + dc + " with bonus of " + bonus);
                    roll += bonus;
                    if (roll >= dc) {
                        IRole rngRole = rollRoles.get(rng.nextInt(rollRoles.size()));
                        for (IRole role : event.getAuthor().getRolesForGuild(event.getGuild()))
                            if (role.getName().contains("lotto-"))
                                event.getAuthor().removeRole(role);
                        event.getAuthor().addRole(rngRole);
                        msg = msg.replace("%rngrole", rngRole.getName());
                        dbGuildUser.setRollsSinceLastClear(0);
                        dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 259200000));
                    } else {
                        dbGuildUser.incrementRollsSinceLastClear();
                        dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 21600000));
                        msg = interaction.getRandomTemplate("fail").getTemplate();
                    }
                    msg = msg.replace("%bonus", String.valueOf(bonus));
                    msg = msg.replace("%roll", String.valueOf(roll));
                    msg = msg.replace("%dc", String.valueOf(dc));
                    dbGuildUser.update();
                } else {
                    msg = interaction.getRandomTemplate("permission").getTemplate();

                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM @ HH:mm");
                msg = msg.replace("%timeremaining", dateFormat.format(dbGuildUser.getRollTimeout()));
                return msg;
            } else {
                return interaction.getRandomTemplate("noroles").getTemplate();
            }
        }
        return "This cannot be used in a private message!";
    }
}
