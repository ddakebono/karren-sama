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

import org.frostbite.karren.Karren;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.LinkedList;

public class InstantReplay {
    private IGuild guild;
    private IVoiceChannel channel;
    private IRAudioReceiver receiver;
    private IRAudioProvider provider;
    private LinkedList<AudioFrame> audioFrames = new LinkedList<>();

    public InstantReplay(IGuild guild, IVoiceChannel channel){
        this.guild = guild;
        this.channel = channel;
    }

    public void writeUserAudioFrame(AudioFrame frame){
        audioFrames.add(frame);
    }

    public void startListening(){
        channel.join();
        receiver = new IRAudioReceiver(this);
        provider = new IRAudioProvider(this);
        guild.getAudioManager().subscribeReceiver(receiver);
        guild.getAudioManager().setAudioProvider(provider);
    }

    public AudioFrame getSingleFrame(){
        AudioFrame data = audioFrames.getFirst();
        audioFrames.removeFirst();
        return data;
    }

    public void stopListening(){
        if(receiver!=null) {
            guild.getAudioManager().unsubscribeReceiver(receiver);
            guild.getAudioManager().setAudioProvider(Karren.bot.getGuildMusicManager(guild).getAudioProvider());
            receiver = null;
            channel.leave();
        }
    }
}