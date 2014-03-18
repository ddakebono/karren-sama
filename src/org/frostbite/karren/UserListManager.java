/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */
package org.frostbite.karren;

public class UserListManager extends Thread{
    public static boolean isInChannel = false;
    public static int getUserIndex(String nick){
        int result = 0;
        for(int i=0; i<GlobalVars.userListNicks.length; i++){
            if(GlobalVars.userListNicks[i].equalsIgnoreCase(nick)){
                result = i;
            }
        }
        return result;
    }
    public static void rmUser(String nick){
        int userIndex = getUserIndex(nick);
        int target = 0;
        String[] userListNickCopy = new String[GlobalVars.userCap-1];
        KarrenCon[] userListCopy = new KarrenCon[GlobalVars.userCap-1];
        for(int i=0; i<GlobalVars.userCap; i++){
            if(i!=userIndex){
                userListNickCopy[target] = GlobalVars.userListNicks[i];
                userListCopy[target] = GlobalVars.userList[i];
                target++;
            }
        }
        GlobalVars.curUserCount--;
        GlobalVars.userListNicks = userListNickCopy;
        GlobalVars.userList = userListCopy;
    }
    public static void addUser(KarrenCon user, String nick){
        GlobalVars.userList[GlobalVars.curUserCount] = user;
        GlobalVars.userListNicks[GlobalVars.curUserCount] = nick;
        GlobalVars.curUserCount++;
    }
    /*public void run(){
        while(!Karren.bot.isConnected() || !isInChannel){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Load current users into userlist
        Object[] data = new String[1];
        Date date = new Date();
        ArrayList<String> returned = new ArrayList<String>();
        GlobalVars.userList = new KarrenCon[GlobalVars.userCap];
        GlobalVars.userListNicks = new String[GlobalVars.userCap];
        Channel botChannel = Karren.bot.getUserBot().getChannels().first();
        for(User load : botChannel.getUsers()){
            if(!load.getNick().equalsIgnoreCase(GlobalVars.botname)){
                data[0] = load.getNick();
                try {
                    returned = MySQLConnector.sqlPush("user", "login", data);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                UserListManager.addUser(new KarrenCon(load, GlobalVars.npChannel, date.getTime(), Boolean.parseBoolean(returned.get(1)), Long.parseLong(returned.get(2)), Long.parseLong(returned.get(3)), Boolean.parseBoolean(returned.get(4)), Boolean.parseBoolean(returned.get(5))), load.getNick());
            }
        }
    }*/
}
