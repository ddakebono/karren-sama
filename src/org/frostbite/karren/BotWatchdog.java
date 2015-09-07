package org.frostbite.karren;

import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;

import java.io.IOException;

public class BotWatchdog extends Thread{
    Logger log;
    KarrenBot bot;
    int timeSinceLastPing = 0;
    boolean hasBotStarted = false;

    public BotWatchdog(Logger log, KarrenBot bot){
        this.log = log;
        this.bot = bot;
    }
    public void run() {
        log.info("Watchdog initialized, monitoring bot status!");
        if(!hasBotStarted) {
            try {
                log.info("Watchdog is trying to start the bot...");
                bot.startBot();
                hasBotStarted = true;
            } catch (IOException | IrcException e) {
                log.error("An error has occured and the bot could not be started!", e);
            }
        }
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
            bot.recoverNick();

        }
    }
    public KarrenBot getBot(){return bot; }
    public void resetPingCount(){
        timeSinceLastPing = 0;
    }
}
