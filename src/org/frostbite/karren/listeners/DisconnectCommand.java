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
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.DiscordDisconnectedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

public class DisconnectCommand implements IListener<DiscordDisconnectedEvent> {
    @Override
    public void handle(DiscordDisconnectedEvent discordDisconnectedEvent) {
        if(!Karren.bot.isKill()){
            try {
                Karren.log.error("A websocket issue has occurred, attempting to reconnect to Discord...");
                Karren.bot.getClient().logout();
                Karren.bot.getClient().login();
            } catch (HTTP429Exception | DiscordException e) {
                Karren.log.error("An error occured while attempting to restart the Discord connection! Bot terminated!");
                Karren.bot.killBot("Internal");
                e.printStackTrace();
            }
        }
    }
}
