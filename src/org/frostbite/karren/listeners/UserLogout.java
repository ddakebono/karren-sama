/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.KarrenCon;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.QuitEvent;

/**
 * Created by frostbite on 1/21/14.
 */
public class UserLogout extends ListenerAdapter{
    public void onLogout(QuitEvent event){
        for(int i=0; i<GlobalVars.userList.size(); i++){
            if(GlobalVars.userList.get(i).getUserObject().getNick().equals(event.getUser().getNick())){
                GlobalVars.userList.remove(i);
            }
        }
    }

}
