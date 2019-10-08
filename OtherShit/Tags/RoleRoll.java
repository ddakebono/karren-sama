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

import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Permission;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import java.sql.Timestamp;
import java.util.*;
import java.util.Random;
import java.util.stream.Collectors;

public class RoleRoll extends Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        Guild guild = result.getEvent().getGuild().block();

        Channel channel = result.getEvent().getMessage().getChannel().block();
        if(channel!=null && channel.getType().equals(Channel.Type.GUILD_TEXT) && guild!=null && Karren.bot.client.getSelfId().isPresent()) {
            Member ourMember = guild.getMemberById(Karren.bot.client.getSelfId().get()).block();
            if(result.getEvent().getMember().isPresent() && ourMember!=null) {
                Member author = result.getEvent().getMember().get();
                DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(guild, author);
                Boolean hasPerm = ourMember.getRoles().any(role -> role.getPermissions().contains(Permission.MANAGE_ROLES)).block();
                if (hasPerm != null && !hasPerm)
                    return interaction.getRandomTemplate("noroleperm").getTemplate();
                List<Role> rollRoles = new LinkedList<>(Objects.requireNonNull(guild.getRoles().filter(role -> role.getName().contains("lotto-")).collectList().block()));
                if (rollRoles.size() > 0) {
                    if (dbGuildUser.getRollTimeout() == null || new Timestamp(System.currentTimeMillis()).after(dbGuildUser.getRollTimeout())) {
                        dbGuildUser.incrementTotalRolls();
                        java.util.Random rng = new Random();
                        int roll = rng.nextInt(100);
                        int bonus = (dbGuildUser.getRollsSinceLastClear() / 2);
                        int dc = (Karren.bot.getSql().getGuild(guild).getRollDifficulty() >= 0 ? Karren.bot.getSql().getGuild(guild).getRollDifficulty() : 95);
                        Karren.log.info("Rolled " + roll + " against a DC of " + dc + " with bonus of " + bonus);
                        roll += bonus;
                        List<Role> authorRoles = author.getRoles().collectList().block();
                        if(authorRoles != null)
                            return "A bad thing happened, please try again later.";
                        if (interaction.hasParameter() && authorRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase("bot-manager")) && interaction.getParameter().equalsIgnoreCase("test"))
                            roll = 100;
                        if (roll >= dc) {
                            //PermissionUtils.isUserHigher(event.getGuild(), event.getClient().getOurUser(), event.getAuthor())
                            if (PermissionUtils.isUserHigher(event.getGuild(), event.getClient().getOurUser(), rollRoles)) {
                                if (roll == 100 && Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange() > 0) {
                                    msg = interaction.getRandomTemplate("winrar").getTemplate();
                                    msg = interaction.replaceMsg(msg, "%guildrange", Integer.toString(Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange()));
                                    List<IUser> userList = event.getGuild().getUsers();
                                    int userPos = userList.indexOf(event.getAuthor());
                                    for (int i = userPos - Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange(); i < userPos + Karren.bot.getSql().getGuild(event.getGuild()).getRandomRange(); i++) {
                                        try {
                                            randomizer(userList.get(i), event, rng);
                                        } catch (IndexOutOfBoundsException ignored) {
                                        }
                                    }
                                } else {
                                    msg = interaction.replaceMsg(msg, "%rngrole", randomizer(event.getAuthor(), event, rng));
                                }
                                dbGuildUser.setRollsSinceLastClear(0);
                                dbGuildUser.incrementWinningRolls();
                                dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 259200000));
                            } else {
                                //Cannot change users role
                                return interaction.getRandomTemplate("higherroles").getTemplate();
                            }
                        } else {
                            if (roll - bonus == 0) {
                                for (IRole role : event.getAuthor().getRolesForGuild(event.getGuild())) {
                                    if (role.getName().contains("lotto-"))
                                        event.getAuthor().removeRole(role);
                                }
                            }
                            dbGuildUser.incrementRollsSinceLastClear();
                            dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 21600000));
                            msg = interaction.getRandomTemplate("fail").getTemplate();
                        }
                        msg = interaction.replaceMsg(msg, "%bonus", String.valueOf(bonus));
                        msg = interaction.replaceMsg(msg, "%roll", String.valueOf(roll - bonus));
                        msg = interaction.replaceMsg(msg, "%total", String.valueOf(roll));
                        msg = interaction.replaceMsg(msg, "%dc", String.valueOf(dc));
                        dbGuildUser.update();
                    } else {
                        msg = interaction.getRandomTemplate("permission").getTemplate();

                    }
                    msg = interaction.replaceMsg(msg, "%timeremaining", KarrenUtil.calcTimeDiff(dbGuildUser.getRollTimeout().getTime(), System.currentTimeMillis()));
                    return msg;
                } else {
                    return interaction.getRandomTemplate("noroles").getTemplate();
                }
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
