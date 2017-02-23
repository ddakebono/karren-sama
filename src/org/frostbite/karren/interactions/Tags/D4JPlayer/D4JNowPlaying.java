/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.D4JPlayer;

import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class D4JNowPlaying implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getGuildMusicManager(event.getGuild()).player.getPlayingTrack()!=null){
            msg = msg.replace("%d4jtitle", Karren.bot.getGuildMusicManager(event.getGuild()).player.getPlayingTrack().getInfo().title);
        } else {
            msg = interaction.getRandomTemplatesFail();
        }
        return msg;
    }
}
