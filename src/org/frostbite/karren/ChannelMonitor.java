/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Objects;

public class ChannelMonitor extends Thread {

    boolean kill = false;

    @Override
    public void run() {
        kill = false;
        while(!kill){
            for(Guild guild : Karren.bot.getClient().getGuilds()){
                if(Objects.requireNonNull(guild.getMember(Karren.bot.client.getSelfUser())).hasPermission(Permission.MANAGE_SERVER)) {
                    List<Category> categoryList = guild.getCategoriesByName("temp voice channels", true);
                    if (categoryList.size() == 1) {
                        List<Invite> invites = guild.retrieveInvites().complete();
                        for (VoiceChannel chan : categoryList.get(0).getVoiceChannels()) {
                            if (invites.stream().noneMatch(x -> Objects.requireNonNull(x.getChannel()).getId().equals(chan.getId())) || invites.isEmpty()) {
                                if (guild.getVoiceStates().stream().anyMatch(x -> Objects.equals(x.getChannel(), chan)))
                                    chan.createInvite().setMaxAge(3600).setMaxUses(1).setTemporary(false).setUnique(true).complete();
                                else
                                    chan.delete().complete();
                            }
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
