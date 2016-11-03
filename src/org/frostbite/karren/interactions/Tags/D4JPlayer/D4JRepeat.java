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

import net.dv8tion.d4j.player.MusicPlayer;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.interactions.Tag;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

public class D4JRepeat implements Tag {

    @Override
    public String handleTemplate(String msg, Interaction interaction, MessageBuilder response, MessageReceivedEvent event) {
        if(Karren.bot.getClient().getConnectedVoiceChannels().size()>0){
            MusicPlayer player = (MusicPlayer)event.getMessage().getGuild().getAudioManager().getAudioProvider();
            if(player.isRepeat()) {
                player.setRepeat(false);
                msg = msg.replace("%bool", "off");
            } else {
                player.setRepeat(true);
                msg = msg.replace("%bool", "on");
            }
        }
        return msg;
    }
}
