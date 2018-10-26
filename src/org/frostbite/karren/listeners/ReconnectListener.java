/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.frostbite.karren.Karren;

import java.util.function.Consumer;

public class ReconnectListener implements Consumer<ReconnectEvent> {
    @Override
    public void accept(ReconnectEvent event) {
        //TODO AR - Look at changes for old code

        event.getClient().updatePresence(Presence.online(Activity.playing("KarrenSama Ver." + Karren.botVersion)));
    }
}
