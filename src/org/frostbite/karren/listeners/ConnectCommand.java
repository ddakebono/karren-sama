/*
 * Copyright (c) 2017 Owen Bennett.
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
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;

import java.util.Properties;

import static org.frostbite.karren.Karren.conf;

public class ConnectCommand extends ListenerAdapter{

    @Override
    public void onConnect(ConnectEvent event){
        //Initialize database connection pool
        Karren.log.info("Initializing Yank database pool");
        Properties dbSettings = new Properties();
        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", conf.getSqluser());
        dbSettings.setProperty("password", conf.getSqlpass());

        Karren.bot.initExtras();

        if(Karren.conf.getAllowSQLRW())
            Yank.setupDefaultConnectionPool(dbSettings);

    }
}
