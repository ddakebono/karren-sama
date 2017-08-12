/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database.Objects;

import org.knowm.yank.Yank;

public class DbGuild {
    private String guildID;
    private String guildName;
    private String guildOwner;
    private String commandPrefix;
    private int rollDifficulty;
    private boolean allowTempChannels;

    public DbGuild(){}

    public DbGuild(String guildID, String guildName, String guildOwner, String commandPrefix) {
        this.guildID = guildID;
        this.guildName = guildName;
        this.guildOwner = guildOwner;
        this.commandPrefix = commandPrefix;
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

    public boolean isAllowTempChannels() {
        return allowTempChannels;
    }

    public void setAllowTempChannels(boolean allowTempChannels) {
        this.allowTempChannels = allowTempChannels;
    }

    //Update db entry
    public void update(){
        String sql = "UPDATE Guild SET GuildOwner=?, GuildName=?, CommandPrefix=?, RollDifficulty=?, AllowTempChannels=? WHERE GuildID=?";
        Yank.execute(sql, new Object[]{guildOwner, guildName, commandPrefix, rollDifficulty, allowTempChannels, guildID});
    }
}
