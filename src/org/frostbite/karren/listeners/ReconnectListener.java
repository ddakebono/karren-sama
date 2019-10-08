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

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;

public class ReconnectListener extends ListenerAdapter {

    @Override
    public void onReconnect(@Nonnull ReconnectedEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing("KarrenSama Ver." + Karren.botVersion));
    }
}
