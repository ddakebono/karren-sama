package org.frostbite.karren;

import org.slf4j.Logger;

public class BotWatchdog extends Thread{
    Logger log;
    KarrenBot bot;
    int timeSinceLastPing = 0;
    public BotWatchdog(Logger log, KarrenBot bot){
        this.log = log;
        this.bot = bot;
    }
    public void run() {
        log.info("Bot Watchdog started!");
        //Increment timeSinceLastPing by one second and check current amount.
        while(!bot.isBotKill()){
            timeSinceLastPing++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(timeSinceLastPing > 240)
                bot.killBot(bot, "Watchdog", true);
            //Make sure the bot stays in the channel.
            if(bot.getUserBot().getChannels().size() == 0 && !bot.isBotKill())
                bot.sendIRC().joinChannel(bot.getBotConf().getChannel());
            //Ensure the bot always has it's configured nick
            recoverNick();

        }
    }
    public boolean recoverNick(){
        if(!bot.getNick().equalsIgnoreCase(bot.getBotConf().getBotname())) {
            bot.sendIRC().changeNick(bot.getBotConf().getBotname());
            bot.sendIRC().identify(bot.getBotConf().getNickservPass());
            return true;
        } else {
            return false;
        }
    }
    public void resetPingCount(){
        timeSinceLastPing = 0;
    }
}
