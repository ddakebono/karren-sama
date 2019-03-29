/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags.D4JPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionEmbedFields;
import org.frostbite.karren.Interactions.Tag;
import org.frostbite.karren.Karren;
import org.frostbite.karren.KarrenUtil;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;

public class D4JList extends Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getClient().getConnectedVoiceChannels().size()>0){
            GuildMusicManager gmm = Karren.bot.getGuildMusicManager(event.getGuild());
            interaction.addEmbedField(new InteractionEmbedFields(0,
                    "\u25B6 " + gmm.player.getPlayingTrack().getInfo().title,
                    KarrenUtil.getMinSecFormattedString(gmm.player.getPlayingTrack().getPosition()) + " - " + KarrenUtil.getMinSecFormattedString(Karren.bot.getGuildMusicManager(event.getGuild()).player.getPlayingTrack().getDuration()), false
            ));
            for(AudioTrack source : Karren.bot.getGuildMusicManager(event.getGuild()).scheduler.getQueue()){
                interaction.addEmbedField(new InteractionEmbedFields(0,
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

    @Override
    public EnumSet<Permissions> getRequiredPermissions() {
        return EnumSet.of(Permissions.SEND_MESSAGES);
    }

}
