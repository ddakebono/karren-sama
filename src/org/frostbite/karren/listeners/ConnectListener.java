package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;

public class ConnectListener extends ListenerAdapter<PircBotX> {
    public void onConnect(ConnectEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        while(bot.getUserBot().getChannels().size()==0 || bot.areThreadsInitialized()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bot.initThreads();
        bot.startThreads();
        bot.sendIRC().joinChannel(bot.getBotConf().getChannel());
    }
}
