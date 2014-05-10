package org.frostbite.karren;

import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.space.SpaceController;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by frostbite on 17/03/14.
 */
public class KarrenBot extends PircBotX {
    //private MySQLConnector sql;
    private ListenCast lc;
    private BotConfiguration botConf;
    private MySQLInterface sql;
    private ArrayList<Interactions> interactions;
    private Logger log;
    private SpaceController space;
    private boolean botKilled = false;
    private boolean threadsInitialized = false;
    public KarrenBot(Configuration<PircBotX> config, BotConfiguration botConf, Logger log){
        super(config);
        this.botConf = botConf;
        this.log = log;
        sql = new MySQLInterface(botConf, log);
        interactions = loadInteractions();
    }
    public void initThreads(){
        threadsInitialized = true;
        space = new SpaceController(sql, this);
        lc = new ListenCast(this, botConf);
    }
    public void startThreads(){
        if(threadsInitialized){
            lc.start();
            space.start();
        } else {
            log.error("Threads must be initialized prior to being started!");
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
        log.debug("Initializing interactions!");
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
        interactions.clear();
        interactions = loadInteractions();
    }
    public boolean isBotKill(){return botKilled;}
    public void botIsKill(){botKilled = true;}
    public ArrayList<Interactions> getInteractions(){return interactions;}
    public MySQLInterface getSql(){return sql;}
    public BotConfiguration getBotConf(){return botConf;}
    public boolean areThreadsInitialized(){return threadsInitialized;}
    public void terminateThreads() throws SQLException {
        lc.kill();
        space.killController();
        lc = null;
        space = null;
        threadsInitialized = false;
    }
    public ListenCast getListenCast(){return lc;}
    public Logger getLog(){return log;}
    public SpaceController getSpace(){return space;}
}