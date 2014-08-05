package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.frostbite.karren.OsSpecific.SystemService;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class SCRAMCommand extends ListenerAdapter<PircBotX>{
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        if(event.getMessage().equals(bot.getBotConf().getCommandPrefix() + "SCRAM") && (event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser()))){
            event.getChannel().send().message("SCRAM activated, stopping all monitored services! The bot will be stopped once shutdown is complete.");
            bot.getLog().info("SCRAM triggered! Terminating services.");
            if(bot.getServices() != null) {
                for (SystemService kill : bot.getServices()) {
                    switch (bot.getOsType()) {
                        case 1:
                            try {
                                kill.winStop();
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                kill.linStop();
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
            event.getChannel().send().message("Service shutdown complete, terminating bot...");
            bot.getLog().info("Service SCRAM complete, stopping bot!");
            bot.killBot(bot, "SCRAM", false);
        }
    }
}
