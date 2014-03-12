/*
 * Copyright (c) 2014 ripxfrostbite.
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.KarrenCon;
import org.frostbite.karren.MySQLConnector;
import org.frostbite.karren.UserListManager;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by frostbite on 1/21/14.
 */
public class UserLogin extends ListenerAdapter{
    public void onLogin(JoinEvent event){
        UserListManager.isInChannel = true;
        Date date = new Date();
        String[] data = new String[1];
        ArrayList<String> returned = new ArrayList<String>();
        if(!event.getUser().getNick().equals(GlobalVars.botname)){
            data[0] = event.getUser().getNick();
            try {
                returned = MySQLConnector.sqlPush("user", "login", data);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            UserListManager.addUser(new KarrenCon(event.getUser(), event.getChannel(), date.getTime(), Boolean.parseBoolean(returned.get(1)), Long.parseLong(returned.get(2)), Long.parseLong(returned.get(3)), Boolean.parseBoolean(returned.get(4)), Boolean.parseBoolean(returned.get(5))), event.getUser().getNick());
        }
    }
}
