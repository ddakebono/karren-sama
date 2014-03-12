/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by frostbite on 3/11/14.
 */
public class InteractionsManager extends ListenerAdapter{
    public static void onMessage(MessageEvent event){
        String msg = event.getMessage();
        String command = ".listen";
        if(msg.toLowerCase().startsWith(command) && event.getUser().isIrcop()){
            msg.replace(command, "");
        }
    }
}
