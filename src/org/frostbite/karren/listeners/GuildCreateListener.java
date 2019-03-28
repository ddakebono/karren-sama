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

import discord4j.core.event.domain.guild.GuildCreateEvent;
import org.frostbite.karren.Karren;

import java.util.function.Consumer;

public class GuildCreateListener implements Consumer<GuildCreateEvent> {
    @Override
    public void accept(GuildCreateEvent guildCreateEvent) {
        //Generate gms
        Karren.bot.createGuildMusicManager(guildCreateEvent.getGuild());
        Karren.log.info("Guild " + guildCreateEvent.getGuild().getName() + " has been registered with the database, and a GuildMusicManager has been spawned.");
    }
}
