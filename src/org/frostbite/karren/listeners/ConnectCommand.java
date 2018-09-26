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

import org.frostbite.karren.Karren;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class ConnectCommand implements IListener<ReadyEvent>{

    @Override
    public void handle(ReadyEvent event){
        //Start auto reminder
        Karren.bot.getAr().start();

        //Start ChannelMonitor
        Karren.bot.getCm().start();

        if(!Karren.conf.isTestMode())
            event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "KarrenSama Ver." + Karren.botVersion);
        else
            event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "TEST MODE - " + Karren.jarProps.getProperty("git.build.version"));
    }
}
