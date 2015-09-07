package org.frostbite.karren;

import java.util.Scanner;

public class ConsoleInputThread extends Thread {
    private BotWatchdog watchdog;
    public ConsoleInputThread(BotWatchdog watchdog){
        this.watchdog = watchdog;
    }
    public void run(){
        String command = "";
        if(!watchdog.getBot().isBotKill()) {
            Scanner in = new Scanner(System.in);
            command = in.next();
        }
        while(!watchdog.getBot().isBotKill()){
            if(command.equalsIgnoreCase("kill")){
                watchdog.getBot().killBot(watchdog.getBot(), "Console User", false);
            } else if(command.equalsIgnoreCase("restart")){
                watchdog.getBot().killBot(watchdog.getBot(), "Console User", true);
            }
        }
    }
}
