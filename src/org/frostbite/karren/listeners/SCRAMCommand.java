package org.frostbite.karren.listeners;

import org.frostbite.karren.KarrenBot;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class SCRAMCommand extends ListenerAdapter<PircBotX>{
    public void onMessage(MessageEvent event){
        KarrenBot bot = (KarrenBot)event.getBot();
        if(event.getMessage().equals(bot.getBotConf().getCommandPrefix() + "SCRAM") && (event.getChannel().isOp(event.getUser()) || event.getChannel().isOwner(event.getUser()))){
            event.getChannel().send().message("SCRAM activated, stopping all monitored services! The bot will be stopped once shutdown is complete.");
            bot.getLog().info("SCRAM triggered! Terminating services.");
            event.getChannel().send().message("Service shutdown complete, terminating bot...");
            bot.getLog().info("Service SCRAM complete, stopping bot!");
            bot.killBot(bot, "SCRAM", false);
        }
    }
}
