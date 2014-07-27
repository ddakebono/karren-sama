package org.frostbite.karren;

import org.frostbite.karren.OsSpecific.SystemService;
import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.space.SpaceController;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class KarrenBot extends PircBotX {
    //private MySQLConnector sql;
    private ListenCast lc;
    private BotConfiguration botConf;
    private MySQLInterface sql;
    private ArrayList<Interactions> interactions;
    private Mailer mail;
    private Logger log;
    private SpaceController space;
    private boolean botKilled = false;
    private boolean threadsInitialized = false;
    private int osType = 0;
    private OutputWindow out;
    private boolean enableServiceControl = false;
    private ArrayList<SystemService> services;
    public KarrenBot(Configuration<PircBotX> config, BotConfiguration botConf, Logger log, int osType, OutputWindow out, boolean enableServiceControl){
        super(config);
        this.botConf = botConf;
        this.log = log;
        this.osType = osType;
        this.enableServiceControl = enableServiceControl;
        try {
            mail = new Mailer(botConf);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
        sql = new MySQLInterface(botConf, log);
        interactions = loadInteractions();
        if(enableServiceControl)
            services = loadServices();
        this.out = out;    }
    public void initThreads(){
        threadsInitialized = true;
        space = new SpaceController(sql, this);
        lc = new ListenCast(this, botConf);
    }
    public void startThreads(){
        if(threadsInitialized){
            if(botConf.getEnableListencast().equalsIgnoreCase("true"))
                lc.start();
            if(botConf.getEnableSpaceController().equalsIgnoreCase("true"))
                space.start();
        } else {
            log.error("Threads must be initialized prior to being started!");
        }
    }
    private ArrayList<SystemService> loadServices(){
        String buffer;
        String[] temp1;
        String name;
        String ident;
        log.debug("Initializing monitored Services!");
        ArrayList<SystemService> services = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("conf/Services.txt"));
            buffer = in.readLine();
            while(buffer!=null){
                temp1 = buffer.split(":");
                if(temp1[0].equalsIgnoreCase("Service")){
                    ident = temp1[1];
                    name = temp1[2];
                    services.add(new SystemService(name , ident));
                }
                buffer = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return services;
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
    public void reloadServices(){
        services.clear();
        services = loadServices();
    }
    public void killBot(KarrenBot bot, String killer, boolean restart){
        bot.getLog().info("Bot has been killed by " + killer);
        botKilled = true;
        if(!restart) {
            bot.stopBotReconnect();
            if (bot.getWindow() != null)
                bot.getWindow().destroyWindow();
        }
        bot.sendIRC().quitServer("Kill command fired, bot terminating.");
    }
    public Mailer getMail(){return mail;}
    public ArrayList<SystemService> getServices(){return services;}
    public int getOsType(){return osType;}
    public boolean isBotKill(){return botKilled;}
    public ArrayList<Interactions> getInteractions(){return interactions;}
    public MySQLInterface getSql(){return sql;}
    public BotConfiguration getBotConf(){return botConf;}
    public boolean areThreadsInitialized(){return threadsInitialized;}
    public void terminateThreads() throws SQLException {
        if(lc!=null && space!=null) {
            lc.kill();
            space.killController();
            lc = null;
            space = null;
        }
        threadsInitialized = false;
    }
    public ListenCast getListenCast(){return lc;}
    public Logger getLog(){return log;}
    public SpaceController getSpace(){return space;}
    public OutputWindow getWindow(){return out;}
    public boolean isEnableServiceControl(){return enableServiceControl;}
}