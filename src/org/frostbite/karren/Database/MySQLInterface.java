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

import org.frostbite.karren.Database.Objects.DbGuild;
import org.frostbite.karren.Database.Objects.DbGuildUser;
import org.frostbite.karren.Database.Objects.DbUser;
import org.frostbite.karren.Database.Objects.DbWordcount;
import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import java.util.HashMap;
import java.util.Map;

public class MySQLInterface {

    //Database object cache
    private Map<String, DbGuild> dbGuildCache = new HashMap<>();
    private Map<String, DbGuildUser> dbGuildUserCache = new HashMap<>();
    private Map<String, DbUser> dbUserCache = new HashMap<>();
    private Map<String, DbWordcount> dbWordcountCache = new HashMap<>();

    public DbGuild getGuild(IGuild guild){
        if(Karren.conf.getAllowSQLRW()){
            if(!dbGuildCache.containsKey(guild.getStringID())) {
                String sql = "INSERT IGNORE Guild (GuildID, GuildOwner, GuildName, CommandPrefix, RollDifficulty) VALUES (?, ?, ?, null, -1)";
                Yank.execute(sql, new Object[]{guild.getStringID(), guild.getOwner().getName(), guild.getName()});
                sql = "SELECT * FROM Guild WHERE GuildID=?";
                DbGuild dbGuild = Yank.queryBean(sql, DbGuild.class, new Object[]{guild.getStringID()});
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
                    String sql = "INSERT IGNORE GuildUser (GuildUserID, UserID, GuildID, RollTimeout, IgnoreCommands) VALUES (null, ?, ?, null, false)";
                    Yank.execute(sql, new Object[]{user.getLongID(), guild.getStringID()});
                    sql = "SELECT * FROM GuildUser WHERE UserID=? AND GuildID=?";
                    DbGuildUser dbGuildUser = Yank.queryBean(sql, DbGuildUser.class, new Object[]{user.getLongID(), guild.getStringID()});
                    dbGuildUserCache.put(guild.getStringID() + user.getStringID(), dbGuildUser);
                    return dbGuildUser;
                } else {
                    return dbGuildUserCache.get(guild.getStringID() + user.getStringID());
                }
            } else {
                return new DbGuildUser(0, 0, "0", null, false, true);
            }
        }
        return null;
    }

    public DbWordcount getWordCount(String word){
        if(Karren.conf.getAllowSQLRW()) {
            if(!dbWordcountCache.containsKey(word)) {
                String sql = "INSERT IGNORE WordCounts (WordID, Word, Count, CountStarted) VALUES (null, ?, 1, null)";
                Yank.execute(sql, new Object[]{word});
                sql = "SELECT * FROM WordCounts WHERE Word=?";
                DbWordcount dbWordcount = Yank.queryBean(sql, DbWordcount.class, new Object[]{word});
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
                Yank.execute(sql, new Object[]{user.getStringID(), user.getName()});
                //UserRecord dbUser = sqlConn.selectFrom(User.USER).where(User.USER.USERID.equalIgnoreCase(user.getStringID())).fetchOne();
                sql = "SELECT * FROM User WHERE UserID=?";
                DbUser dbUser = Yank.queryBean(sql, DbUser.class, new Object[]{user.getStringID()});
                dbUserCache.put(user.getStringID(), dbUser);
                return dbUser;
            } else {
                return dbUserCache.get(user.getStringID());
            }
        }
        return null;
    }
}
