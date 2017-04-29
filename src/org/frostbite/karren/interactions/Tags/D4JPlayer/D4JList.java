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

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class D4JList implements Tag {
    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getClient().getConnectedVoiceChannels().size()>0){
            StringBuilder list = new StringBuilder();
            for(AudioTrack source : (AudioTrack[])Karren.bot.getGuildMusicManager(event.getGuild()).scheduler.getQueue().toArray()){
                list.append(source.getInfo().title).append("\n");
            }
            list = new StringBuilder(list.substring(0, list.length() - 2));
            msg = msg.replace("%nplist", list.toString());
        }
        return msg;
    }
}
