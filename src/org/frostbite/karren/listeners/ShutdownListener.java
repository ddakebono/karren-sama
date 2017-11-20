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

import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

public class ShutdownListener implements IListener<DisconnectedEvent> {
    @Override
    public void handle(DisconnectedEvent disconnectedEvent) {
        //Lets release the Yank SQL connection pool
        if(disconnectedEvent.getReason().equals(DisconnectedEvent.Reason.LOGGED_OUT)) {
            Karren.log.info("Releasing SQL connection pool");
            Yank.releaseAllConnectionPools();
        }
        Karren.bot.getAr().setSuspend(true);
    }
}
