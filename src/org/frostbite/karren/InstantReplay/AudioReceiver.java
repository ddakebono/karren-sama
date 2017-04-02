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
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.obj.IUser;

public class AudioReceiver implements IAudioReceiver {

    private InstantReplay ir;

    public AudioReceiver(InstantReplay ir){
        this.ir = ir;
    }

    @Override
    public void receive(byte[] bytes, IUser iUser) {
        if(!iUser.isBot()){
            Karren.log.debug("Incoming audio frame from " + iUser.getName() + " Size: " + bytes.length);
        }
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.OPUS;
    }
}
