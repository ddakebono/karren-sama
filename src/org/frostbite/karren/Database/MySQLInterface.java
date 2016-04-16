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

import org.frostbite.karren.Database.Models.tables.*;
import org.frostbite.karren.Database.Models.tables.records.SongdbRecord;
import org.frostbite.karren.Database.Models.tables.records.UserfavesRecord;
import org.frostbite.karren.Database.Models.tables.records.UsersRecord;
import org.frostbite.karren.Database.Models.tables.records.WordCountsRecord;
import org.frostbite.karren.Karren;
import org.frostbite.karren.interactions.Interaction;
import org.frostbite.karren.listencast.Song;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BooleanSupplier;

public class MySQLInterface {

    private DSLContext sqlConn;

    public MySQLInterface(){
        refreshSQLConnection();
    }

    private void refreshSQLConnection(){
        try {
            this.sqlConn = DSL.using(DriverManager.getConnection("jdbc:mysql://" + Karren.conf.getSqlhost() + ":" + Karren.conf.getSqlport() + "/" + Karren.conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8", Karren.conf.getSqluser(), Karren.conf.getSqlpass()), SQLDialect.MARIADB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WordCountsRecord getWordCount(String word){
        refreshSQLConnection();
        sqlConn.insertInto(WordCounts.WORD_COUNTS).values(null, word, 1, new Timestamp(new Date().getTime())).onDuplicateKeyIgnore().execute();
        return sqlConn.selectFrom(WordCounts.WORD_COUNTS).where(WordCounts.WORD_COUNTS.WORD.equalIgnoreCase(word)).fetchOne();
    }


    public UsersRecord getUserData(String nick){
        refreshSQLConnection();
        sqlConn.insertInto(Users.USERS).values(null, nick, false, 0).onDuplicateKeyIgnore().execute();
        return sqlConn.selectFrom(Users.USERS).where(Users.USERS.USER.equalIgnoreCase(nick)).fetchOne();
    }

    public Result<UserfavesRecord> getUserFaves(int songid){
        refreshSQLConnection();
        return sqlConn.selectFrom(Userfaves.USERFAVES).where(Userfaves.USERFAVES.SONGID.equal(songid)).fetch();
    }

    public void addUserFave(String userid, Song song){
        refreshSQLConnection();
        sqlConn.insertInto(Userfaves.USERFAVES).values(null, userid, song.getSongID()).onDuplicateKeyIgnore().execute();
        song.addFave();
    }

    public SongdbRecord getSong(String name){
        refreshSQLConnection();
        sqlConn.insertInto(Songdb.SONGDB).values(null, name, 0, 0, 0, 0, false).onDuplicateKeyIgnore().execute();
        return sqlConn.selectFrom(Songdb.SONGDB).where(Songdb.SONGDB.SONGTITLE.equal(name)).fetchOne();
    }

    public void updateDJActivity(String curDJ, String streamName){
        refreshSQLConnection();
        sqlConn.update(RadioDj.RADIO_DJ).set(RadioDj.RADIO_DJ.ACTIVE, 0).execute();
        if(curDJ!=null&&curDJ.length()>0) {
            sqlConn.insertInto(RadioDj.RADIO_DJ).values(null, curDJ, curDJ, streamName, "default", 1).onDuplicateKeyUpdate().set(RadioDj.RADIO_DJ.ACTIVE, 1).set(RadioDj.RADIO_DJ.STREAMNAME, streamName).execute();
        }
    }
}
