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

import discord4j.core.event.domain.lifecycle.DisconnectEvent;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

import java.util.function.Consumer;

public class ShutdownListener implements Consumer<DisconnectEvent> {
    @Override
    public void accept(DisconnectEvent event) {
        //Lets release the Yank SQL connection pool
        Karren.log.info("Releasing SQL connection pool");
        Yank.releaseAllConnectionPools();
    }
}
