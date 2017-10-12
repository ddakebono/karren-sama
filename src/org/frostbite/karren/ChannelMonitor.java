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

import sx.blah.discord.handle.obj.ICategory;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ChannelMonitor extends Thread {

    boolean kill = false;

    @Override
    public void run() {
        while(!kill){
            for(IGuild guild : Karren.bot.getClient().getGuilds()){
                ICategory category = guild.getCategories().stream().filter(x -> x.getName().toLowerCase().contains("temp voice channels")).findFirst().orElse(null);
                if(category!=null) {
                    for (IVoiceChannel channel : category.getVoiceChannels()){
                        if(channel.getExtendedInvites().stream().filter(x -> x.getInviter().equals(Karren.bot.getClient().getOurUser())).count()==0){
                            if(channel.getConnectedUsers().size()>0){
                                channel.createInvite(3600, 1, false, true);
                                break;
                            }
                            Karren.log.debug("Channel " + channel.getName() + " has expired, deleting.");
                            channel.delete();
                        }
                    }
                }
            }
            try {
                //Check for expired channels every 30 minutes
                if(Karren.conf.isTestMode())
                    Thread.sleep(5000);
                else
                    Thread.sleep(1800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        kill = true;
    }
}
