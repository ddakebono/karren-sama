/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.KarrenCon;
import org.frostbite.karren.MySQLConnector;
import org.frostbite.karren.UserListManager;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.QuitEvent;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by frostbite on 1/21/14.
 */
public class UserLogout extends ListenerAdapter{
    public void onLogout(QuitEvent event){
        Object[] data = new Object[2];
        for(int i=0; i<GlobalVars.curUserCount; i++){
            if(GlobalVars.userListNew[i].getUserObject().getNick().equals(event.getUser().getNick())){
                //setTimeWasted(GlobalVars.userList.get(i));
                data[0] = event.getUser().getNick();
                data[1] = GlobalVars.userListNew[i];
                try {
                    MySQLConnector.sqlPush("user", "logout", data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                UserListManager.rmUser(event.getUser().getNick());
            }
        }
    }
    private static void setTimeWasted(KarrenCon user){

    }
}
