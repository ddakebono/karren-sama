package org.frostbite.karren;

import org.frostbite.karren.InterConnect.InterConnectListener;
import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.*;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.EventDispatcher;
import sx.blah.discord.util.HTTP429Exception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KarrenBot {
    IDiscordClient client;
    MySQLInterface sql = new MySQLInterface();
    ListenCast lc;
    boolean extrasReady = false;
    ArrayList<Interactions> interactions;
    InterConnectListener interConnectListener;

    public KarrenBot(IDiscordClient client){
        this.client = client;
    }

    public void initDiscord(){
        EventDispatcher ed = client.getDispatcher();
        ed.registerListener(new ConnectCommand());
        ed.registerListener(new HelpCommand());
        ed.registerListener(new InteractionCommands());
        ed.registerListener(new HueCommand());
        ed.registerListener(new KillCommand());
        ed.registerListener(new MiscCommands());
        try {
            client.login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Interactions> loadInteractions(){
        String buffer;
        String[] temp1;
        String ident;
        String[] tags;
        String response;
        int confidence;
        String[] activators;
        ArrayList<Interactions> interactions = new ArrayList<>();
        Karren.log.debug("Initializing interactions!");
        try {
            BufferedReader in = new BufferedReader(new FileReader("conf/Interactions.txt"));
            buffer = in.readLine();
            while(buffer!=null){
                temp1 = buffer.split(":");
                if(temp1[0].equalsIgnoreCase("Interactions")){
                    ident = temp1[1];
                    response = temp1[3];
                    activators = temp1[2].split("[,]\\s*");
                    tags = temp1[4].split("[,]\\s*");
                    confidence = Integer.parseInt(temp1[5]);
                    interactions.add(new Interactions(ident, tags, response, activators, confidence));
                }
                buffer = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return interactions;
    }

    public void reloadInteractions(){
        interactions = loadInteractions();
    }

    public void initExtras(){
        interactions = loadInteractions();
        lc = new ListenCast(client);
        interConnectListener = new InterConnectListener(Karren.log);
        extrasReady = true;
    }

    public boolean startThreads(){
        if(extrasReady) {
            lc.start();
            interConnectListener.start();
            return true;
        } else {
            return false;
        }
    }

    public void killBot(String killer){
        lc.kill();
        try {
            client.logout();
        } catch (HTTP429Exception | DiscordException e) {
            e.printStackTrace();
        }
        Karren.log.info("Bot has been killed by " + killer);
        System.exit(0);
    }

    /*
    GETTERS
     */

    public MySQLInterface getSql(){
        return sql;
    }

    public ArrayList<Interactions> getInteractions(){
        return interactions;
    }

    public IDiscordClient getClient(){
        return client;
    }

    public ListenCast getListenCast(){
        return lc;
    }
}
