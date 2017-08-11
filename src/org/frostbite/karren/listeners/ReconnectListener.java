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
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.ReconnectSuccessEvent;
import sx.blah.discord.util.DiscordException;

import java.util.Properties;

import static org.frostbite.karren.Karren.conf;

public class ReconnectListener implements IListener<ReconnectSuccessEvent> {
    @Override
    public void handle(ReconnectSuccessEvent reconnectSuccessEvent) {
        Karren.log.info("Reconnected to Discord, reconnecting to database!");
        Properties dbSettings = new Properties();
        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", conf.getSqluser());
        dbSettings.setProperty("password", conf.getSqlpass());

        Yank.setupDefaultConnectionPool(dbSettings);

        Karren.bot.getAr().setSuspend(false);

        try {
            reconnectSuccessEvent.getClient().online("KarrenSama Ver." + Karren.botVersion);
        } catch (DiscordException e) {
            Karren.log.error(e.getErrorMessage());
        }
    }
}
