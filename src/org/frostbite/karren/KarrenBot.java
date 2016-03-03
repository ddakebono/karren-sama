package org.frostbite.karren;

import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.ConnectCommand;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.EventDispatcher;

import java.util.ArrayList;

public class KarrenBot {
    IDiscordClient client;
    MySQLInterface sql = new MySQLInterface();
    ListenCast lc;
    boolean threadsInit = false;
    ArrayList<Interactions> interactions = new ArrayList<>();

    public KarrenBot(IDiscordClient client){
        this.client = client;
    }

    public void initDiscord(){
        EventDispatcher ed = client.getDispatcher();
        ed.registerListener(new ConnectCommand());
        try {
            client.login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    public void initThreads(){
        lc = new ListenCast(client);
        threadsInit = true;
    }

    public boolean startThreads(){
        if(threadsInit) {
            lc.start();
            return true;
        } else {
            return false;
        }
    }

    public MySQLInterface getSql(){
        return sql;
    }
}
