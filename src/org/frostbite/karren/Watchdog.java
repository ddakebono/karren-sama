/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import org.knowm.yank.Yank;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Watchdog extends Thread {
    boolean kill = false;
    int checkFailedMax;
    int failedChecks = 0;
    public int watchdogInterventions = 0;

    public Watchdog(int checkFailedMax) {
        this.checkFailedMax = checkFailedMax;
    }

    @Override
    public void run() {
        if(Karren.bot==null)
            startBot();
        while (!kill){
            if(!checkForLife()) {
                failedChecks++;
            } else {
                failedChecks = 0;
            }

            if(failedChecks>=checkFailedMax){
                Karren.log.info("Watchdog detected a problem with the bot, restarting!");
                watchdogInterventions++;
                //Shutdown bot completely and restart
                cleanupBot();
                Karren.log.info("**** Waiting 10 seconds to allow remaining threads to die... ****");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startBot();
                failedChecks = 0;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkForLife(){
        if(!Karren.bot.getAr().isAlive() && Karren.conf.getEnableInteractions())
            return false;
        if(!Karren.bot.getClient().isLoggedIn())
            return false;
        if(Karren.bot.getGuildManager().getTagHandlers().size()==0 && Karren.conf.getEnableInteractions())
            return false;
        return true;
    }

    public void cleanupBot(){
        Karren.bot.isKill = true;
        //Unhook and shutdown interaction system
        Karren.bot.client.getDispatcher().unregisterListener(Karren.bot.interactionListener);
        Karren.bot.ic.loadDefaultInteractions();
        Yank.releaseAllConnectionPools();
        //Interactions reset to default state and unregistered
        if(Karren.bot.ar.isAlive())
            Karren.bot.ar.setKill(true);
        if(Karren.bot.client.isReady()) {
            try {
                Karren.bot.client.logout();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }

        Karren.bot = null;
        System.gc();
    }

    public void startBot(){
        Karren.log.info("Watchdog is now starting the bot...");

        //Build our discord client
        IDiscordClient client = null;
        try {
            client = new ClientBuilder().withToken(Karren.conf.getDiscordToken()).withRecommendedShardCount().build();
        } catch (DiscordException e) {
            e.printStackTrace();
        }

        //Setup the objects we need.
        Karren.bot = new KarrenBot(client);

        //Pass execution to main bot class.
        Karren.bot.initDiscord();
    }

}
