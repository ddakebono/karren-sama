/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;

public class InstantReplay extends Thread {
    private IGuild guild;

    public InstantReplay(IGuild guild){
        this.guild = guild;
    }

    @Override
    public void run(){

    }
}
