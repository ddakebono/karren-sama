package org.frostbite.karren;

import org.frostbite.karren.listencast.ListenCast;
import org.frostbite.karren.listeners.*;
import org.pircbotx.ChannelListEntry;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by frostbite on 17/03/14.
 */
public class KarrenBot extends PircBotX {
    private MySQLConnector sql;
    private ListenCast lc;
    BotConfiguration botConf;
    public KarrenBot(Configuration<PircBotX> config, BotConfiguration botConf){
        super(config);
        this.botConf = botConf;
        lc = new ListenCast(this);
        sql = new MySQLConnector(this);
    }
    public void startBot() throws IOException, IrcException {
        lc.start();
        super.startBot();
    }
    public BotConfiguration getBotConf(){return botConf;}
    public void killListencast(){
        lc.kill();
    }
}