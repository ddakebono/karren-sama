/*
 * Copyright (c) 2021 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class RoleRoll extends Tag {

    boolean rolePermissionError = false;

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        rolePermissionError = false;
        if (result.getEvent().isFromGuild()) {
            Member ourMember = result.getEvent().getGuild().getMemberById(Karren.bot.client.getSelfUser().getId());
            if (ourMember != null) {
                DbGuildUser dbGuildUser = Karren.bot.getSql().getGuildUser(result.getEvent().getGuild(), result.getEvent().getAuthor());
                if (ourMember.getRoles().stream().noneMatch(role -> role.getPermissions().contains(Permission.MANAGE_ROLES)))
                    return interaction.getRandomTemplate("noroleperm").getTemplate();
                List<Role> rollRoles = result.getEvent().getGuild().getRoles().stream().filter(role -> role.getName().contains("lotto-")).collect(Collectors.toList());
                if (rollRoles.size() > 0) {
                    if (dbGuildUser.getRollTimeout() == null || new Timestamp(System.currentTimeMillis()).after(dbGuildUser.getRollTimeout())) {
                        dbGuildUser.incrementTotalRolls();
                        java.util.Random rng = new Random();
                        int roll = rng.nextInt(100);
                        int bonus = (dbGuildUser.getRollsSinceLastClear() / 2);
                        int dc = (Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRollDifficulty() >= 0 ? Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRollDifficulty() : 95);
                        Karren.log.info("Rolled " + roll + " against a DC of " + dc + " with bonus of " + bonus);
                        roll += bonus;
                        List<Role> authorRoles = Objects.requireNonNull(result.getEvent().getMember()).getRoles();
                        if (interaction.hasParameter() && authorRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase("bot-manager")) && interaction.getParameter().equalsIgnoreCase("test"))
                            roll = 100;
                        if (roll >= dc) {
                            //PermissionUtils.isUserHigher(event.getGuild(), event.getClient().getOurUser(), event.getAuthor())
                            if (roll == 100 && Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRandomRange() > 0) {
                                msg = interaction.getRandomTemplate("winrar").getTemplate();
                                msg = interaction.replaceMsg(msg, "%guildrange", Integer.toString(Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRandomRange()));
                                List<Member> userList = result.getEvent().getGuild().getMembers();
                                int userPos = userList.indexOf(result.getEvent().getMember());
                                for (int i = userPos - Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRandomRange(); i < userPos + Karren.bot.getSql().getGuild(result.getEvent().getGuild()).getRandomRange(); i++) {
                                    try {
                                        randomizer(userList.get(i), result.getEvent(), rng, ourMember);
                                    } catch (IndexOutOfBoundsException ignored) {
                                    }
                                }
                            } else {
                                msg = interaction.replaceMsg(msg, "%rngrole", randomizer(result.getEvent().getMember(), result.getEvent(), rng, ourMember));
                            }
                            if (!rolePermissionError) {
                                dbGuildUser.setRollsSinceLastClear(0);
                                dbGuildUser.incrementWinningRolls();
                                dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + 259200000));
                            } else {
                                msg = interaction.getRandomTemplate("higherroles").getTemplate();
                            }
                        } else {
                            if (roll - bonus == 0) {
                                for (Role role : result.getEvent().getMember().getRoles()) {
                                    if (ourMember.canInteract(role) && !rolePermissionError) {
                                        if (role.getName().contains("lotto-")) {
                                            AuditableRestAction roleChange = result.getEvent().getGuild().removeRoleFromMember(result.getEvent().getMember(), role);
                                            roleChange.queue();
                                        }
                                    } else {
                                        rolePermissionError = true;
                                    }
                                }
                            }
                            DbGuild guild = Karren.bot.getSql().getGuild(result.getEvent().getGuild());
                            dbGuildUser.incrementRollsSinceLastClear();
                            if(dbGuildUser.getRollsSinceLastClear()>dbGuildUser.getHighestRollFail())
                                dbGuildUser.setHighestRollFail(dbGuildUser.getRollsSinceLastClear());
                            dbGuildUser.setRollTimeout(new Timestamp(System.currentTimeMillis() + (3600000L *guild.getRollTimeoutHours())));
                            if (!rolePermissionError)
                                msg = interaction.getRandomTemplate("fail").getTemplate();
                            else
                                msg = interaction.getRandomTemplate("higherroles2").getTemplate();
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

    private String randomizer(Member user, MessageReceivedEvent event, Random rng, Member ourUser) {
        List<Role> rollRoles = event.getGuild().getRoles().stream().filter(x -> x.getName().contains("lotto-")).collect(Collectors.toList());
        for (Role role : user.getRoles()) {
            if (role.getName().contains("lotto-")) {
                if (ourUser.canInteract(role)) {
                    AuditableRestAction roleChange = event.getGuild().removeRoleFromMember(user, role);
                    roleChange.queue();
                    rollRoles.remove(role);
                } else {
                    rolePermissionError = true;
                }
            }

        }
        Role rngRole = rollRoles.get(rng.nextInt(rollRoles.size()));
        if (!rolePermissionError && ourUser.canInteract(rngRole)) {
            AuditableRestAction roleChange = event.getGuild().addRoleToMember(user, rngRole);
            roleChange.queue();
        } else {
            rolePermissionError = true;
        }
        return rngRole.getName();
    }

    @Override
    public String getTagName() {
        return "roleroll";
    }

    @Override
    public Permission[] getRequiredPermissions() {
        return new Permission[]{Permission.MANAGE_ROLES, Permission.MESSAGE_WRITE};
    }
}
