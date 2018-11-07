/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.PermissionUtils;

import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RoleRoll extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(!event.getMessage().getChannel().isPrivate()) {
            DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(event.getGuild(), event.getAuthor());
            List<IRole> rollRoles = new LinkedList<>();
            if(!PermissionUtils.hasPermissions(event.getGuild(), event.getClient().getOurUser(), Permissions.MANAGE_ROLES))
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
                    if(interaction.hasParameter() && event.getAuthor().getRolesForGuild(event.getGuild()).stream().anyMatch(x -> x.getName().equalsIgnoreCase("bot-manager")) && interaction.getParameter().equalsIgnoreCase("test"))
                        roll = 100;
                    if (roll >= dc) {
                        //PermissionUtils.isUserHigher(event.getGuild(), event.getClient().getOurUser(), event.getAuthor())
                        if(PermissionUtils.isUserHigher(event.getGuild(), event.getClient().getOurUser(), rollRoles)) {
                            if(roll==100 && Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange()>0){
                                msg = interaction.getRandomTemplate("winrar").getTemplate();
                                msg = interaction.replaceMsg(msg, "%guildrange", Integer.toString(Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange()));
                                List<IUser> userList = event.getGuild().getUsers();
                                int userPos = userList.indexOf(event.getAuthor());
                                for(int i=userPos-Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange(); i<userPos+Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange(); i++ ){
                                    try {
                                        randomizer(userList.get(i), event, rng);
                                    } catch (IndexOutOfBoundsException ignored){ }
                                }
                            } else {
                                msg = interaction.replaceMsg(msg,"%rngrole", randomizer(event.getAuthor(), event, rng));
                            }
                            dbGuildUser.setRollsSinceLastClear(0);
                            dbGuildUser.incrementWinningRolls();
                            dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 259200000));
                        } else {
                            //Cannot change users role
                            return interaction.getRandomTemplate("higherroles").getTemplate();
                        }
                    } else {
                        if(roll-bonus==0){
                            for(IRole role : event.getAuthor().getRolesForGuild(event.getGuild())){
                                if(role.getName().contains("lotto-"))
                                    event.getAuthor().removeRole(role);
                            }
                        }
                        dbGuildUser.incrementRollsSinceLastClear();
                        dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 21600000));
                        msg = interaction.getRandomTemplate("fail").getTemplate();
                    }
                    msg = interaction.replaceMsg(msg,"%bonus", String.valueOf(bonus));
                    msg = interaction.replaceMsg(msg,"%roll", String.valueOf(roll-bonus));
                    msg = interaction.replaceMsg(msg,"%total", String.valueOf(roll));
                    msg = interaction.replaceMsg(msg,"%dc", String.valueOf(dc));
                    dbGuildUser.update();
                } else {
                    msg = interaction.getRandomTemplate("permission").getTemplate();

                }
                msg = interaction.replaceMsg(msg,"%timeremaining", KarrenUtil.calcTimeDiff(dbGuildUser.getRollTimeout().getTime(), System.currentTimeMillis()));
                return msg;
            } else {
                return interaction.getRandomTemplate("noroles").getTemplate();
            }
        }
        return "This cannot be used in a private message!";
    }

    private String randomizer(IUser user, MessageReceivedEvent event, Random rng){
        List<IRole> rollRoles = event.getGuild().getRoles().stream().filter(x -> x.getName().contains("lotto-")).collect(Collectors.toList());
        for (IRole role : user.getRolesForGuild(event.getGuild())) {
            if (role.getName().contains("lotto-")) {
                event.getAuthor().removeRole(role);
                rollRoles.remove(role);
            }
        }
        IRole rngRole = rollRoles.get(rng.nextInt(rollRoles.size()));
        event.getAuthor().addRole(rngRole);
        return rngRole.getName();
    }

    @Override
    public String getTagName() {
        return "roleroll";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.MANAGE_ROLES);
    }
}
