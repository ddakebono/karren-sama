/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.VoicePingEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;

import java.util.ConcurrentModificationException;

public class VoiceExitCommand implements IListener<VoicePingEvent> {
    @Override
    public void handle(VoicePingEvent voicePingEvent) {
        try {
            for (IVoiceChannel channel : voicePingEvent.getClient().getConnectedVoiceChannels()) {
                try {
                    if (channel.getGuild().getAudioChannel().getQueueSize() == 0) {
                        channel.leave();
                    }
                } catch (DiscordException e) {
                    e.printStackTrace();
                }
            }
        } catch (ConcurrentModificationException ignored){}
    }
}
