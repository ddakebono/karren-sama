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
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.obj.IUser;

public class AudioReceiver implements IAudioReceiver {
    @Override
    public void receive(byte[] bytes, IUser iUser) {

    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.PCM;
    }
}
