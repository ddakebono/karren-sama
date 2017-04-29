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

import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.obj.IUser;

public class IRAudioProvider implements IAudioProvider {

    private final InstantReplay ir;
    private AudioFrame lastFrame;
    private IUser user;

    public IRAudioProvider(InstantReplay ir, IUser user) {
        this.ir = ir;
        this.user = user;
    }

    @Override
    public boolean isReady() {
        if(lastFrame == null){
            lastFrame = ir.getSingleFrame(user);
        }
        return lastFrame != null;
    }

    @Override
    public byte[] provide() {
        if (lastFrame == null) {
            lastFrame = ir.getSingleFrame(user);
        }

        byte[] data = lastFrame != null ? lastFrame.audioData : null;
        //Temp patch to kill playback after list empties
        lastFrame = null;
        return data;
    }

    @Override
    public int getChannels() {
        return 2;
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.OPUS;
    }
}
