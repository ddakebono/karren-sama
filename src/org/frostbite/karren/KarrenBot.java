package org.frostbite.karren;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FilenameUtils;
import org.frostbite.karren.InterConnect.InterConnectListener;
import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.*;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.EventDispatcher;
import sx.blah.discord.util.HTTP429Exception;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

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
        if(Boolean.parseBoolean(Karren.conf.getConnectToDiscord())) {
            EventDispatcher ed = client.getDispatcher();
            ed.registerListener(new ConnectCommand());
            ed.registerListener(new HelpCommand());
            ed.registerListener(new InteractionCommands());
            ed.registerListener(new KillCommand());
            try {
                client.login();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            startThreads();
        }
    }

    private ArrayList<Interactions> loadInteractions(){
        Gson gson = new Gson();
        ArrayList<Interactions> result = new ArrayList<>();
        File intDir = new File("conf/Interactions");
        if(intDir.isDirectory()){
            File[] intFiles = getFilesInFolders(intDir);
            for(File file : intFiles){
                try {
                    Interactions tempInteraction = gson.fromJson(new FileReader(file), Interactions.class);
                    tempInteraction.setIdentifier(FilenameUtils.removeExtension(file.getName()));
                    result.add(tempInteraction);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            Karren.log.info("No Interactions detected, interaction system unregistered.");
            client.getDispatcher().unregisterListener(new InteractionCommands());
            return result;
        }
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

    @SuppressWarnings("ConstantConditions")
    public File[] getFilesInFolders(File directory){
        ArrayList<File> files = new ArrayList<>();
        if(directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.isDirectory())
                    Collections.addAll(files, getFilesInFolders(file));
                else
                    files.add(file);
            }
            return files.toArray(new File[files.size()]);
        } else {
            return files.toArray(new File[files.size()]);
        }
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
