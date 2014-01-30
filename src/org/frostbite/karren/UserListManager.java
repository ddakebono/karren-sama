/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren;

/**
 * Created by frostbite on 1/30/14.
 */
public class UserListManager {
    public static int getUserIndex(String nick){
        int result = 0;
        boolean found = false;
        for(int i=0; i<GlobalVars.userListNicks.length; i++){
            if(GlobalVars.userListNicks[i].equalsIgnoreCase(nick)){
                result = i;
            }
        }
        return result;
    }
    public static void rmUser(String nick){
        int userIndex = getUserIndex(nick);
        //KarrenCon[] userListCopy
    }
}
