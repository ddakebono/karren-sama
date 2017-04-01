/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.InstantReplay;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashMap;

public class InstantReplayManager {
    private HashMap<String, InstantReplay> guildReplays = new HashMap<>();

    /*
    User refers to the user that sent the start listening command
    Used to get the bot to join the right voice channel
    */
    public InstantReplay getInstantReplay(IGuild guild, IUser user){
        if(user.getVoiceStateForGuild(guild).getChannel()!=null) {
            if (!guildReplays.containsKey(guild.getID())) {
                guildReplays.put(guild.getID(), new InstantReplay(guild, user.getVoiceStateForGuild(guild).getChannel()));
            }
            return guildReplays.get(guild.getID());
        }
        return null;
    }

    public void stopInstantReplay(IGuild guild){
        guildReplays.get(guild.getID()).stopListening();
        guildReplays.remove(guild.getID());
    }
}
