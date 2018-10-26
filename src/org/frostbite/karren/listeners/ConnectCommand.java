/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

import java.util.Properties;
import java.util.function.Consumer;

import static org.frostbite.karren.Karren.conf;

public class ConnectCommand implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent event) {
        //Initialize database connection pool
        Karren.log.info("Initializing Yank database pool");
        Properties dbSettings = new Properties();
        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", conf.getSqluser());
        dbSettings.setProperty("password", conf.getSqlpass());

        if(Karren.conf.getAllowSQLRW())
            Yank.setupDefaultConnectionPool(dbSettings);

        //TODO Start auto reminder

        //TODO Start ChannelMonitor

        if(!Karren.conf.isTestMode())
            event.getClient().updatePresence(Presence.online(Activity.playing("KarrenSama Ver." + Karren.botVersion)));
        else
            event.getClient().updatePresence(Presence.online(Activity.playing("TEST MODE - " + Karren.botVersion)));
    }
}
