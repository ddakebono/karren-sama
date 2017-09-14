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
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
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
        filter.setMode(PUBGMode.valueOf(params[1].trim()));
        filter.setRegion(PUBGRegion.na);
        filter.setSeason(PUBGSeason.PRE4_2017);
        Player player;
        try {
            player = pubg.getByNickname(params[0].trim(), filter);
        } catch (IllegalArgumentException e){
            return interaction.getRandomTemplate("fail").getTemplate();
        }

        //Get stats
        EmbedBuilder embed = new EmbedBuilder();
        embed.withAuthorName("Playerunknown's Battleground Stats");
        msg = msg.replace("%username", player.getPlayerName());
        embed.withDescription(msg);
        embed.withColor(Color.RED);
        embed.appendField("Kills", pubg.getPlayerMatchStatByStatName(player, PUBGStat.KILLS).getDisplayValue(), false);
        embed.appendField("Skill Rating", pubg.getPlayerMatchStatByStatName(player, PUBGStat.RATING).getDisplayValue(), false);
        embed.appendField("Rounds Played", pubg.getPlayerMatchStatByStatName(player, PUBGStat.ROUNDS_PLAYED).getDisplayValue(), false);
        embed.appendField("Kill to Death Ratio", pubg.getPlayerMatchStatByStatName(player, PUBGStat.KILL_DEATH_RATIO).getDisplayValue(), false);
        embed.appendField("Rounds in Top 10", pubg.getPlayerMatchStatByStatName(player, PUBGStat.TOP_10).getDisplayValue(), false);
        embed.appendField("Winner Winners", pubg.getPlayerMatchStatByStatName(player, PUBGStat.WINS).getDisplayValue(), false);
        embed.appendField("Better Luck Next Times", pubg.getPlayerMatchStatByStatName(player, PUBGStat.LOSSES).getDisplayValue(), false);
        embed.withFooterText("Requested by: " + event.getAuthor().getName());
        response.withEmbed(embed.build());
        return null;
    }

    @Override
    public String getTagName() {
        return "battlegrounds";
    }

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES, Permissions.EMBED_LINKS);
    }
}
