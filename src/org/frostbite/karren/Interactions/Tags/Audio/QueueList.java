/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.Audio;

import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.AudioPlayer.PlaylistSong;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;

public class QueueList extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        int songCount = 0;

        if(result.getEvent().getGuild().getAudioManager().isConnected()){
            GuildMusicManager gmm = Karren.bot.getGuildMusicManager(result.getEvent().getGuild());
            interaction.addEmbedField(new InteractionEmbedFields(
                    "\u25B6 " + gmm.player.getPlayingTrack().getInfo().title,
                    KarrenUtil.getMinSecFormattedString(gmm.player.getPlayingTrack().getPosition()) + " - " + KarrenUtil.getMinSecFormattedString(Karren.bot.getGuildMusicManager(result.getEvent().getGuild()).player.getPlayingTrack().getDuration()), false
            ));

            msg = interaction.replaceMsg(msg, "%queueCount", Integer.toString(gmm.scheduler.getQueue().getSize()));
            msg = interaction.replaceMsg(msg, "%currentPosition", Integer.toString((gmm.scheduler.getQueue().getPlayedSongs())));

            for(int i=gmm.scheduler.getQueue().getCurrentSongIndex()+1; i<gmm.scheduler.getQueue().getSize(); i++){
                songCount++;
                if(songCount>14)
                    break;

                PlaylistSong song = gmm.scheduler.getQueue().getSongs().get(i);

                interaction.addEmbedField(new InteractionEmbedFields(
                        song.track.getInfo().title,
                        "Creator: " + song.track.getInfo().author + ", Length: " + KarrenUtil.getMinSecFormattedString(song.track.getInfo().length) + ", Streaming link: " + (song.track.getInfo().isStream?"Yes":"No"),
                        false
                ));
            }
        } else {
            msg = interaction.getRandomTemplate("noqueue").getTemplate();
        }
        return msg;
    }

    @Override
    public String getTagName() {
        return "d4jlist";
    }

}
