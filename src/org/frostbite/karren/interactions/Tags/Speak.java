/*
 * Copyright (c) 2016 Owen Bennett.
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
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;

import java.util.Optional;

public class Speak implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        Optional<IVoiceChannel> voiceOpt = event.getMessage().getAuthor().getVoiceChannel();
        if(voiceOpt.isPresent() || interaction.getChannel()!=null) {
            IVoiceChannel voice = voiceOpt.get();
            if(interaction.getChannel()!=null)
                voice = Karren.bot.getClient().getVoiceChannelByID(interaction.getChannel());
            try {
                if (!voice.isConnected() || (voice.isConnected() && voice.getGuild().getAudioChannel().getQueueSize() == 0)) {
                    voice.join();
                    voice.getGuild().getAudioChannel().setVolume(interaction.getVoiceVolume());
                    voice.getGuild().getAudioChannel().queueFile(interaction.getRandomVoiceFile());
                } else {
                    msg = interaction.getRandomTemplatesFail();
                }
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }
}
