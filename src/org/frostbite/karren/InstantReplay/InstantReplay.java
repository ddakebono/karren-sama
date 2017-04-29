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

import org.frostbite.karren.AudioPlayer.AudioProvider;
import org.frostbite.karren.Karren;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InstantReplay {
    private IGuild guild;
    private IVoiceChannel channel;
    private IRAudioReceiver receiver;
    private HashMap<Long, LinkedList<AudioFrame>> usersAudioFrames = new HashMap<>();
    private ArrayList<String> lockedUsers = new ArrayList<>();

    public InstantReplay(IGuild guild, IVoiceChannel channel){
        this.guild = guild;
        this.channel = channel;
    }

    public void writeUserAudioFrame(AudioFrame frame){
        if(!lockedUsers.contains(frame.user.getStringID())) {
            LinkedList<AudioFrame> audioFrames = usersAudioFrames.getOrDefault(frame.user.getLongID(), new LinkedList<>());
            audioFrames.add(frame);
            if (audioFrames.size() > 1500)
                audioFrames.removeFirst();
            if (!usersAudioFrames.containsKey(frame.user.getLongID()))
                usersAudioFrames.put(frame.user.getLongID(), audioFrames);
        }
    }

    public void startListening(){
        channel.join();
        receiver = new IRAudioReceiver(this);
        guild.getAudioManager().subscribeReceiver(receiver);
    }

    public AudioFrame getSingleFrame(IUser playback){
        AudioFrame data = null;
        LinkedList<AudioFrame> audioFrames = usersAudioFrames.getOrDefault(playback.getLongID(), null);
        if(audioFrames!=null && !audioFrames.isEmpty()) {
            data = audioFrames.getFirst();
            audioFrames.removeFirst();
            Karren.log.debug("List has " + audioFrames.size() + " remaining frames");
        } else {
            //Kill provider and return control to lavaplayer
            guild.getAudioManager().setAudioProvider(new AudioProvider(Karren.bot.getGuildMusicManager(guild).player));
            lockedUsers.remove(playback.getStringID());
        }
        return data;
    }

    public void stopListening(){
        if(receiver!=null) {
            guild.getAudioManager().unsubscribeReceiver(receiver);
            guild.getAudioManager().setAudioProvider(Karren.bot.getGuildMusicManager(guild).getAudioProvider());
            receiver = null;
            channel.leave();
            usersAudioFrames.clear();
        }
    }

    public IVoiceChannel getChannel() {
        return channel;
    }

    public ArrayList<String> getLockedUsers() {
        return lockedUsers;
    }

    public LinkedList<AudioFrame> getList(IUser user){
        return usersAudioFrames.get(user.getLongID());
    }
}