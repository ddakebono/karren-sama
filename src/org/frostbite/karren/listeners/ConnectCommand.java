package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class ConnectCommand implements IListener<ReadyEvent>{

    @Override
    public void handle(ReadyEvent event){
        Karren.log.info("Starting threads!");
        Karren.bot.initExtras();
        if(!Karren.bot.startThreads()){
            Karren.log.error("Problem occured while starting threads, threaded functions will not be available!");
        }
    }
}
