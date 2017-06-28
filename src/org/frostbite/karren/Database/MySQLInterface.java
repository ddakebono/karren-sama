/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database;

import org.frostbite.karren.Database.Objects.*;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySQLInterface {

    //Database object cache
    private Map<String, DbGuild> dbGuildCache = new HashMap<>();
    private Map<String, DbGuildUser> dbGuildUserCache = new HashMap<>();
    private Map<String, DbUser> dbUserCache = new HashMap<>();
    private Map<String, DbWordcount> dbWordcountCache = new HashMap<>();
    private ArrayList<DbReminder> dbReminderCache = new ArrayList<>();

    public DbGuild getGuild(IGuild guild){
        if(Karren.conf.getAllowSQLRW()){
            if(!dbGuildCache.containsKey(guild.getStringID())) {
                String sql = "INSERT IGNORE Guild (GuildID, GuildOwner, GuildName, CommandPrefix, RollDifficulty) VALUES (?, ?, ?, null, -1)";
                Object[] params = {guild.getStringID(), guild.getOwner().getName(), guild.getName()};
                Yank.execute(sql, params);
                sql = "SELECT * FROM Guild WHERE GuildID=?";
                Object[] params2 = {guild.getStringID()};
                DbGuild dbGuild = Yank.queryBean(sql, DbGuild.class, params2);
                dbGuildCache.put(guild.getStringID(), dbGuild);
                return dbGuild;
            } else {
                return dbGuildCache.get(guild.getStringID());
            }
        }
        return null;
    }

    public DbGuildUser getGuildUser(IGuild guild, IUser user){
        if(Karren.conf.getAllowSQLRW()){
            if(guild!=null) {
                if (!dbGuildUserCache.containsKey(guild.getStringID() + user.getStringID())) {
                    DbUser dbuser = getUserData(user);
                    String sql = "INSERT IGNORE GuildUser (GuildUserID, UserID, GuildID, RollTimeout, IgnoreCommands, RollsSinceLastClear, TotalRolls) VALUES (null, ?, ?, null, 0, 0, 0)";
                    Object[] params = {dbuser.getUserID(), guild.getStringID()};
                    Yank.execute(sql, params);
                    sql = "SELECT * FROM GuildUser WHERE UserID=? AND GuildID=?";
                    Object[] params2 = {dbuser.getUserID(), guild.getStringID()};
                    DbGuildUser dbGuildUser = Yank.queryBean(sql, DbGuildUser.class, params2);
                    dbGuildUserCache.put(guild.getStringID() + user.getStringID(), dbGuildUser);
                    return dbGuildUser;
                } else {
                    return dbGuildUserCache.get(guild.getStringID() + user.getStringID());
                }
            } else {
                DbGuildUser dbGuildUser = new DbGuildUser();
                dbGuildUser.setGuildUserID(0);
                dbGuildUser.setUserID(0);
                dbGuildUser.setGuildID("0");
                dbGuildUser.setIgnoreCommands(false);
                dbGuildUser.setRollTimeout(null);
                dbGuildUser.setNotMapped(true);
                dbGuildUser.setRollsSinceLastClear(0);
                dbGuildUser.setTotalRolls(0);
                return dbGuildUser;
            }
        }
        return null;
    }

    public DbWordcount getWordCount(String word){
        if(Karren.conf.getAllowSQLRW()) {
            if(!dbWordcountCache.containsKey(word)) {
                String sql = "INSERT IGNORE WordCounts (WordID, Word, Count, CountStarted) VALUES (null, ?, 1, null)";
                Object[] params = {word};
                Yank.execute(sql, params);
                sql = "SELECT * FROM WordCounts WHERE Word=?";
                DbWordcount dbWordcount = Yank.queryBean(sql, DbWordcount.class, params);
                dbWordcountCache.put(word, dbWordcount);
                return dbWordcount;
            } else {
                return dbWordcountCache.get(word);
            }
        }
        return null;
    }

    public DbUser getUserData(IUser user){
        if(Karren.conf.getAllowSQLRW()) {
            if(!dbUserCache.containsKey(user.getStringID())) {
                String sql = "INSERT IGNORE User (UserID, TimeLeft, DJName, DJPicture, DJActive, DJStreamName) VALUES (?, null, ?, null, false, null)";
                Object[] params = {user.getLongID(), user.getName()};
                Yank.execute(sql, params);
                sql = "SELECT * FROM User WHERE UserID=?";
                Object[] params2 = {user.getLongID()};
                DbUser dbUser = Yank.queryBean(sql, DbUser.class, params2);
                dbUserCache.put(user.getStringID(), dbUser);
                return dbUser;
            } else {
                return dbUserCache.get(user.getStringID());
            }
        }
        return null;
    }

    public DbReminder[] getReminder(IUser target){
        if(Karren.conf.getAllowSQLRW()){
            List<DbReminder> reminders = dbReminderCache.stream().filter(x -> x.getTargetID().equals(target.getStringID())).collect(Collectors.toList());
            if(reminders.size()>0)
                return (DbReminder[]) reminders.toArray();
            String sql = "SELECT * FROM Reminders WHERE UserID=?, ReminderTime=?";

        }
        return null;
    }

}
