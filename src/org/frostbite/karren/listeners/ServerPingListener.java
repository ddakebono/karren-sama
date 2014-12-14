package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ServerPingEvent;

/*
 * This listener exists to tell the watchdog that the bot is still connected, this is to fix the weird issue of the bot disappearing from the network.(I think)
 */

public class ServerPingListener extends ListenerAdapter<PircBotX> {
    public void onServerPing(ServerPingEvent<PircBotX> event){
        ((KarrenBot)event.getBot()).getWatchdog().resetPingCount();
    }
}
