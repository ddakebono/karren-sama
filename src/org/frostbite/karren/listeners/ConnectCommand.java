/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;

public class ConnectCommand implements IListener<GuildCreateEvent> {

    @Override
    public void handle(GuildCreateEvent event){
        if(!Karren.bot.isExtrasReady()) {
            Karren.log.info("Starting threads!");
            Karren.bot.initExtras();
            if (!Karren.bot.startThreads()) {
                Karren.log.error("Problem occured while starting threads, threaded functions will not be available!");
            }
        }
    }
}
