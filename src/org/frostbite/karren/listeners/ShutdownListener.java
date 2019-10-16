/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

import javax.annotation.Nonnull;

public class ShutdownListener extends ListenerAdapter {

    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        //Lets release the Yank SQL connection pool
        Karren.log.info("Releasing SQL connection pool");
        Karren.bot.ar.setKill(true);
        Karren.bot.cm.kill();
        Yank.releaseAllConnectionPools();
    }
}
