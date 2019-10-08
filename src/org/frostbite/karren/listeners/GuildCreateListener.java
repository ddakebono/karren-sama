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

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.frostbite.karren.Karren;

import javax.annotation.Nonnull;

public class GuildCreateListener extends ListenerAdapter {

    @Override
    public void onGuildReady(@Nonnull GuildReadyEvent event) {
        //Karren.bot.createGuildMusicManager(event.getGuild());
        Karren.log.info("Guild " + event.getGuild().getName() + " has been registered with the database, and a GuildMusicManager has been spawned.");
    }
}
