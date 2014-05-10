package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.DisconnectEvent;

import java.sql.SQLException;

/**
 * Created by frostbite on 10/05/14.
 */
public class DisconnectListener extends ListenerAdapter<PircBotX> {
    public void onDisconnect(DisconnectEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        if(!bot.isBotKill()){
            bot.getLog().error("Bot disconnected from server! Reconnecting momentarily...");
        }
        try {
            bot.terminateThreads();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
