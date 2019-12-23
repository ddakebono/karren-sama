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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

public class QueueList extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        if(result.getEvent().getGuild().getAudioManager().isConnected()){
            GuildMusicManager gmm = Karren.bot.getGuildMusicManager(result.getEvent().getGuild());
            interaction.addEmbedField(new InteractionEmbedFields(
                    "\u25B6 " + gmm.player.getPlayingTrack().getInfo().title,
                    KarrenUtil.getMinSecFormattedString(gmm.player.getPlayingTrack().getPosition()) + " - " + KarrenUtil.getMinSecFormattedString(Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).player.getPlayingTrack().getDuration()), false
            ));
            for(AudioTrack source : Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).scheduler.getQueue()){
                interaction.addEmbedField(new InteractionEmbedFields(
                        source.getInfo().title,
                        "Creator: " + source.getInfo().author + ", Length: " + KarrenUtil.getMinSecFormattedString(source.getInfo().length) + ", Streaming link: " + (source.getInfo().isStream?"Yes":"No"),
                        false
                ));
            }
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jlist";
    }

}
