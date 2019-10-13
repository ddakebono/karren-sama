/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

public class NowPlayingTime extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).player.getPlayingTrack()!=null) {
            msg = interaction.replaceMsg(msg,"%position", KarrenUtil.getMinSecFormattedString(Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).player.getPlayingTrack().getPosition()));
            msg = interaction.replaceMsg(msg,"%duration", KarrenUtil.getMinSecFormattedString(Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).player.getPlayingTrack().getDuration()));
        } else {
            msg = interaction.getRandomTemplate("fail").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jnowplayingtime";
    }
}
