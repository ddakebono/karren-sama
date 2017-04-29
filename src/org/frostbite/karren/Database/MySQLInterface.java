/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database;

import org.frostbite.karren.Database.Models.tables.Favorites;
import org.frostbite.karren.Database.Models.tables.Songdb;
import org.frostbite.karren.Database.Models.tables.User;
import org.frostbite.karren.Database.Models.tables.Wordcounts;
import org.frostbite.karren.Database.Models.tables.records.FavoritesRecord;
import org.frostbite.karren.Database.Models.tables.records.SongdbRecord;
import org.frostbite.karren.Database.Models.tables.records.UserRecord;
import org.frostbite.karren.Database.Models.tables.records.WordcountsRecord;
import org.frostbite.karren.Karren;
import org.frostbite.karren.listencast.Song;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import sx.blah.discord.handle.obj.IUser;
import java.sql.*;

public class MySQLInterface {

    private DSLContext sqlConn;

    public MySQLInterface(){
        refreshSQLConnection();
    }

    private void refreshSQLConnection(){
        if(Karren.conf.getAllowSQLRW()) {
            try {
                this.sqlConn = DSL.using(DriverManager.getConnection("jdbc:mysql://" + Karren.conf.getSqlhost() + ":" + Karren.conf.getSqlport() + "/" + Karren.conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8", Karren.conf.getSqluser(), Karren.conf.getSqlpass()), SQLDialect.MARIADB);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public WordcountsRecord getWordCount(String word){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(Wordcounts.WORDCOUNTS).values(null, word, 1, null).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(Wordcounts.WORDCOUNTS).where(Wordcounts.WORDCOUNTS.WORD.equalIgnoreCase(word)).fetchOne();
        } else {
            return null;
        }
    }

    public UserRecord getUserData(IUser user){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(User.USER).values(user.getStringID(), null, user.getName(), null, 0, null).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(User.USER).where(User.USER.USERID.equalIgnoreCase(user.getStringID())).fetchOne();
        } else {
            return null;
        }
    }

    public Result<FavoritesRecord> getUserFaves(int songid){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            return sqlConn.selectFrom(Favorites.FAVORITES).where(Favorites.FAVORITES.SONGID.equal(songid)).fetch();
        } else {
            return null;
        }
    }

    public void addUserFave(String userid, Song song){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(Favorites.FAVORITES).values(null, userid, song.getSongID()).onDuplicateKeyIgnore().execute();
            song.addFave();
        }
    }

    public SongdbRecord getSong(String name){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(Songdb.SONGDB).values(null, name, 0, 0, 0, 0, false).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(Songdb.SONGDB).where(Songdb.SONGDB.SONGTITLE.equal(name)).fetchOne();
        } else {
            return null;
        }
    }

    public void updateDJActivity(String curDJ, String streamName){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            //Switch off all DJ active statuses
            sqlConn.update(User.USER).set(User.USER.DJACTIVE, 0).execute();
            IUser newDJ = Karren.bot.getClient().getUserByID(Long.getLong(curDJ));
            if (newDJ != null) {
                UserRecord userAccount = getUserData(newDJ);
                userAccount.setDjactive(1);
                userAccount.setDjstreamname(streamName);
                userAccount.setDjname(newDJ.getName());
                userAccount.update();
            } else {
                sqlConn.insertInto(User.USER).values(0, null, curDJ, "default", 1, "streamName").onDuplicateKeyUpdate().set(User.USER.DJNAME, curDJ).set(User.USER.DJSTREAMNAME, streamName).set(User.USER.DJACTIVE, 1).execute();
            }
        }
    }
}
