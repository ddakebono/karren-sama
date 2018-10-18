/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.frostbite.karren.Database.Objects.*;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

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

    public DbGuild getGuild(Guild guild){
        if(Karren.conf.getAllowSQLRW()){
            if(!dbGuildCache.containsKey(guild.getId().asString())) {
                String sql = "INSERT IGNORE Guild (GuildID, GuildOwner, GuildName, CommandPrefix, RollDifficulty, MaxVolume, RandomRange, OverrideChannel) VALUES (?, ?, ?, null, -1, 40, 0, 0)";
                Object[] params = {guild.getId().asString(), guild.getOwner().block().getDisplayName(), guild.getName()};
                Yank.execute(sql, params);
                sql = "SELECT * FROM Guild WHERE GuildID=?";
                Object[] params2 = {guild.getId().asString()};
                DbGuild dbGuild = Yank.queryBean(sql, DbGuild.class, params2);
                dbGuildCache.put(guild.getId().asString(), dbGuild);
                return dbGuild;
            } else {
                return dbGuildCache.get(guild.getId().asString());
            }
        }
        return new DbGuild();
    }

    public DbGuildUser getGuildUser(Guild guild, User user){
        if(Karren.conf.getAllowSQLRW()){
            if(guild!=null) {
                if (!dbGuildUserCache.containsKey(guild.getId().asString() + user.getId().asString())) {
                    DbUser dbuser = getUserData(user);
                    String sql = "INSERT IGNORE GuildUser (GuildUserID, UserID, GuildID, RollTimeout, IgnoreCommands, RollsSinceLastClear, TotalRolls, WinningRolls) VALUES (null, ?, ?, null, 0, 0, 0, 0)";
                    Object[] params = {dbuser.getUserID(), guild.getId().asString()};
                    Yank.execute(sql, params);
                    sql = "SELECT * FROM GuildUser WHERE UserID=? AND GuildID=?";
                    Object[] params2 = {dbuser.getUserID(), guild.getId().asString()};
                    DbGuildUser dbGuildUser = Yank.queryBean(sql, DbGuildUser.class, params2);
                    dbGuildUserCache.put(guild.getId().asString() + user.getId().asString(), dbGuildUser);
                    return dbGuildUser;
                } else {
                    return dbGuildUserCache.get(guild.getId().asString() + user.getId().asString());
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
                dbGuildUser.setWinningRolls(0);
                return dbGuildUser;
            }
        }
        return new DbGuildUser();
    }

    public DbGuildUser getGuildUser(int guildUserID){
        if(Karren.conf.getAllowSQLRW()) {
            DbGuildUser guildUser = dbGuildUserCache.values().stream().filter(x -> x.getGuildUserID()==guildUserID).findFirst().orElse(null);
            if(guildUser==null) {
                String sql = "SELECT * FROM GuildUser WHERE GuildUserID=?";
                Object[] params = {guildUserID};
                return Yank.queryBean(sql, DbGuildUser.class, params);
            }
            return guildUser;
        }
        return new DbGuildUser();
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
        return new DbWordcount();
    }

    public DbUser getUserData(User user){
        if(Karren.conf.getAllowSQLRW()) {
            if(!dbUserCache.containsKey(user.getId().asString())) {
                String sql = "INSERT IGNORE User (UserID, TimeLeft, VRCUserID) VALUES (?, null, null)";
                Object[] params = {user.getId().asBigInteger()};
                Yank.execute(sql, params);
                sql = "SELECT * FROM User WHERE UserID=?";
                Object[] params2 = {user.getId().asBigInteger()};
                DbUser dbUser = Yank.queryBean(sql, DbUser.class, params2);
                dbUserCache.put(user.getId().asString(), dbUser);
                return dbUser;
            } else {
                return dbUserCache.get(user.getId().asString());
            }
        }
        return new DbUser();
    }

    public DbReminder[] getReminder(DbGuildUser target){
        if(Karren.conf.getAllowSQLRW()){
            List<DbReminder> reminders = dbReminderCache.stream().filter(x -> x.getTargetID()==(target.getGuildUserID())).collect(Collectors.toList());
            if(reminders.size()>0) {
                DbReminder[] remindArray = new DbReminder[reminders.size()];
                return reminders.toArray(remindArray);
            }
            String sql = "SELECT * FROM Reminder WHERE TargetID=? AND ReminderSent=0";
            Object[] params = {target.getGuildUserID()};
            reminders =  Yank.queryBeanList(sql, DbReminder.class, params);
            dbReminderCache.addAll(reminders);
            return reminders.toArray(new DbReminder[0]);
        }
        return null;
    }

    public void preloadReminderCache(){
        if(Karren.conf.getAllowSQLRW()){
            String sql = "SELECT * FROM Reminder WHERE ReminderSent=0 AND ReminderTime>CURDATE()";
            dbReminderCache.addAll(Yank.queryBeanList(sql, DbReminder.class, null));
        }
    }

    public void cleanReminderCache(){
        if(dbReminderCache.size()>0){
            for(DbReminder item : dbReminderCache){
                if(item.reminderSent)
                    dbReminderCache.remove(item);
            }
        }
    }

    public void addReminder(DbReminder reminder){
        if(Karren.conf.getAllowSQLRW()){
            String sql = "INSERT IGNORE Reminder (ReminderID, AuthorID, TargetID, ReminderTime, Message, ReminderSent, ChannelID) VALUES (null, ?, ?, ?, ?, false, ?)";
            Object[] params = {reminder.getAuthorID(), reminder.getTargetID(), reminder.getReminderTime(), reminder.getMessage(), reminder.getChannelID()};
            reminder.setReminderID(Math.toIntExact(Yank.insert(sql, params)));
            dbReminderCache.add(reminder);
        }
    }

    public List<Object[]> getGuildRollsTop(){
        if(Karren.conf.getAllowSQLRW()) {
            String sql = "SELECT GuildID, SUM(TotalRolls) AS GuildRolls FROM GuildUser GROUP BY GuildID ORDER BY GuildRolls DESC LIMIT 5";
            return Yank.queryObjectArrays(sql, null);
        }
        return null;
    }

    public ArrayList<DbReminder> getDbReminderCache() {
        return dbReminderCache;
    }

    public Map<String, DbGuild> getDbGuildCache() {
        return dbGuildCache;
    }

    public Map<String, DbGuildUser> getDbGuildUserCache() {
        return dbGuildUserCache;
    }

    public Map<String, DbUser> getDbUserCache() {
        return dbUserCache;
    }

    public Map<String, DbWordcount> getDbWordcountCache() {
        return dbWordcountCache;
    }
}
