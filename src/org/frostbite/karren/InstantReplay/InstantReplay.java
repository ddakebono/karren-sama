/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.InstantReplay;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.HashMap;

public class InstantReplay {
    private IGuild guild;
    private IVoiceChannel channel;
    private AudioReceiver receiver;
    private HashMap<String, Byte[]> userAudioFrames;

    public InstantReplay(IGuild guild, IVoiceChannel channel){
        this.guild = guild;
        this.channel = channel;
    }

    public void startListening(){
        channel.join();
        receiver = new AudioReceiver(this);
        guild.getAudioManager().subscribeReceiver(receiver);
    }

    public void stopListening(){
        if(receiver!=null) {
            guild.getAudioManager().unsubscribeReceiver(receiver);
            receiver = null;
            channel.leave();
        }
    }
}
