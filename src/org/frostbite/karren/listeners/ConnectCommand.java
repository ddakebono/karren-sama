/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

import java.util.Properties;

import static org.frostbite.karren.Karren.conf;

public class ConnectCommand implements IListener<ReadyEvent>{

    @Override
    public void handle(ReadyEvent event){
        //Initialize database connection pool
        Properties dbSettings = new Properties();
        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", Karren.conf.getSqluser());
        dbSettings.setProperty("password", Karren.conf.getSqlpass());

        Yank.setupDefaultConnectionPool(dbSettings);

        if(!Karren.bot.isExtrasReady()) {
            Karren.log.info("Starting threads!");
            if (!Karren.bot.startThreads()) {
                Karren.log.error("Problem occured while starting threads, threaded functions will not be available!");
            }
        }
    }
}
