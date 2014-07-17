package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;

public class ConnectListener extends ListenerAdapter<PircBotX> {
    public void onConnect(ConnectEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        while(bot.areThreadsInitialized()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bot.getLog().debug("Connecting to " + bot.getBotConf().getChannel());
        bot.sendIRC().joinChannel(bot.getBotConf().getChannel());
        while(bot.getUserBot().getChannels().size()==0){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bot.initThreads();
        bot.startThreads();
    }
}
