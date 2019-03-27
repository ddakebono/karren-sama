/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database.Objects;

import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

public class DbGuild {
    private String guildID = "";
    private String guildName = "";
    private String guildOwner = "";
    private String commandPrefix;
    private String accessRole = "";
    private int rollDifficulty = -1;
    private int randomRange = 0;
    private int maxVolume = 40;
    private long overrideChannel = 0;

    public DbGuild(){}

    public DbGuild(String guildID, String guildName, String guildOwner, String commandPrefix, String accessRole, int maxVolume, int randomRange, long overrideChannel) {
        this.guildID = guildID;
        this.guildName = guildName;
        this.guildOwner = guildOwner;
        this.commandPrefix = commandPrefix;
        this.accessRole = accessRole;
        this.maxVolume = maxVolume;
        this.randomRange = randomRange;
        this.overrideChannel = overrideChannel;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getGuildOwner() {
        return guildOwner;
    }

    public void setGuildOwner(String guildOwner) {
        this.guildOwner = guildOwner;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public int getRollDifficulty() {
        return rollDifficulty;
    }

    public void setRollDifficulty(int rollDifficulty) {
        this.rollDifficulty = rollDifficulty;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }

    public int getRandomRange() {
        return randomRange;
    }

    public void setRandomRange(int randomRange) {
        this.randomRange = randomRange;
    }

    public long getOverrideChannel() {
        return overrideChannel;
    }

    public void setOverrideChannel(long overrideChannel) {
        this.overrideChannel = overrideChannel;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }

    //Update db entry
    public void update(){
        if(Karren.conf.getAllowSQLRW()) {
            String sql = "UPDATE Guild SET GuildOwner=?, GuildName=?, CommandPrefix=?, RollDifficulty=?, MaxVolume=?, RandomRange=?, OverrideChannel=?, AccessRole=?  WHERE GuildID=?";
            Yank.execute(sql, new Object[]{guildOwner, guildName, commandPrefix, rollDifficulty, maxVolume, randomRange, overrideChannel, accessRole, guildID});
        }
    }
}
