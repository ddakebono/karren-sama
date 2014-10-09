package org.frostbite.karren;

import java.util.Scanner;

public class ConsoleInputThread extends Thread {
    private KarrenBot bot;
    public ConsoleInputThread(KarrenBot bot){
        this.bot = bot;
    }
    public void run(){
        String command = "";
        if(!bot.isBotKill()) {
            Scanner in = new Scanner(System.in);
            command = in.next();
        }
        while(!bot.isBotKill()){
            if(command.equalsIgnoreCase("kill")){
                bot.killBot(bot, "Console User", false);
            } else if(command.equalsIgnoreCase("restart")){
                bot.killBot(bot, "Console User", true);
            }
        }
    }
}
