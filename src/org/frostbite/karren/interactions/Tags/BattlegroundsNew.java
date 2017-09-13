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
import pro.lukasgorny.core.JPubg;
import pro.lukasgorny.dto.FilterCriteria;
import pro.lukasgorny.dto.Player;
import pro.lukasgorny.enums.PUBGMode;
import pro.lukasgorny.enums.PUBGRegion;
import pro.lukasgorny.enums.PUBGSeason;
import pro.lukasgorny.enums.PUBGStat;
import pro.lukasgorny.factory.JPubgFactory;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class BattlegroundsNew extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(!interaction.hasParameter())
            return interaction.getRandomTemplate("noparam").getTemplate();
        String parameter = interaction.getParameter();
        String[] params = parameter.split(",");

        JPubg pubg = JPubgFactory.getWrapper(Karren.conf.getTrackerNetworkAPIKey());
        FilterCriteria filter = new FilterCriteria();
        filter.setMode(PUBGMode.valueOf(params[1]));
        filter.setRegion(PUBGRegion.na);
        filter.setSeason(PUBGSeason.PRE4_2017);
        Player player;
        try {
            player = pubg.getByNickname(params[0].trim(), filter);
        } catch (IllegalArgumentException e){
            return interaction.getRandomTemplate("fail").getTemplate();
        }

        //Get stats
        msg = msg.replace("%username", player.getPlayerName());
        msg = msg.replace("%kills", pubg.getPlayerMatchStatByStatName(player, PUBGStat.KILLS).getDisplayValue());
        msg = msg.replace("%rating", pubg.getPlayerMatchStatByStatName(player, PUBGStat.RATING).getDisplayValue());
        msg = msg.replace("%roundsPlayed", pubg.getPlayerMatchStatByStatName(player, PUBGStat.ROUNDS_PLAYED).getDisplayValue());
        msg = msg.replace("%kdr", pubg.getPlayerMatchStatByStatName(player, PUBGStat.KILL_DEATH_RATIO).getDisplayValue());
        msg = msg.replace("%top10s", pubg.getPlayerMatchStatByStatName(player, PUBGStat.TOP_10).getDisplayValue());
        msg = msg.replace("%wins", pubg.getPlayerMatchStatByStatName(player, PUBGStat.WINS).getDisplayValue());
        msg = msg.replace("%losses", pubg.getPlayerMatchStatByStatName(player, PUBGStat.LOSSES).getDisplayValue());
        return msg;
    }

    @Override
    public String getTagName() {
        return "battlegrounds";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }
}
