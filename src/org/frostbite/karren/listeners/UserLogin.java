/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.KarrenCon;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

/**
 * Created by frostbite on 1/21/14.
 */
public class UserLogin extends ListenerAdapter{
    public void onLogin(JoinEvent event){
        GlobalVars.userList.add(new KarrenCon(event.getUser(), event.getChannel()));
    }
}
