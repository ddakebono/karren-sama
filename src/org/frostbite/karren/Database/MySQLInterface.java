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

import org.frostbite.karren.Database.Models.tables.*;
import org.frostbite.karren.Database.Models.tables.Interaction;
import org.frostbite.karren.Database.Models.tables.records.*;
import org.frostbite.karren.Karren;
import org.frostbite.karren.listencast.Song;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import java.sql.*;
import java.util.ArrayList;

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

    public GuildRecord getGuild(IGuild guild){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            sqlConn.insertInto(Guild.GUILD).values(guild.getStringID(), guild.getOwner().getName(), guild.getName()).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(Guild.GUILD).where(Guild.GUILD.GUILDID.eq(guild.getStringID())).fetchOne();
        }
        return null;
    }

    public Result<InteractionRecord> getInteractionsForGuild(IGuild guild){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            return sqlConn.selectFrom(Interaction.INTERACTION).where(Interaction.INTERACTION.GUILDID.eq(guild.getStringID())).fetch();
        }
        return null;
    }

    public ArrayList<InteractiontemplateRecord> getInteractionSuccessTemplates(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            Result<InteractionsuccesstemplateRecord> returned = sqlConn.selectFrom(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE).where(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
            ArrayList<InteractiontemplateRecord> templates = new ArrayList<>();
            for(InteractionsuccesstemplateRecord templateLink : returned){
                templates.add(getInteractionTemplates(templateLink.getTemplateid()));
            }
            return templates;
        }
        return null;
    }

    public ArrayList<InteractiontemplateRecord> getInteractionFailureTemplates(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            Result<InteractionfailuretemplateRecord> returned = sqlConn.selectFrom(Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE).where(Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
            ArrayList<InteractiontemplateRecord> templates = new ArrayList<>();
            for(InteractionfailuretemplateRecord templateLink : returned){
                templates.add(getInteractionTemplates(templateLink.getTemplateid()));
            }
            return templates;
        }
        return null;
    }

    public ArrayList<InteractiontemplateRecord> getInteractionPermErrorTemplates(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            Result<InteractionpermissionerrortemplateRecord> returned = sqlConn.selectFrom(Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE).where(Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
            ArrayList<InteractiontemplateRecord> templates = new ArrayList<>();
            for(InteractionpermissionerrortemplateRecord templateLink : returned){
                templates.add(getInteractionTemplates(templateLink.getTemplateid()));
            }
            return templates;
        }
        return null;
    }

    private InteractiontemplateRecord getInteractionTemplates(int templateID){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            return sqlConn.selectFrom(Interactiontemplate.INTERACTIONTEMPLATE).where(Interactiontemplate.INTERACTIONTEMPLATE.TEMPLATEID.eq(templateID)).fetchOne();
        }
        return null;
    }

    public ArrayList<InteractiontagRecord> getInteractionTags(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            Result<InteractiontagHasInteractionRecord> returned = sqlConn.selectFrom(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION).where(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
            ArrayList<InteractiontagRecord> tags = new ArrayList<>();
            for(InteractiontagHasInteractionRecord tag : returned){
                tags.add(sqlConn.selectFrom(Interactiontag.INTERACTIONTAG).where(Interactiontag.INTERACTIONTAG.TAGID.eq(tag.getInteractionid())).fetchOne());
            }
            return tags;
        }
        return null;
    }

    public Result<InteractionvoicefileRecord> getVoiceFiles(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            return sqlConn.selectFrom(Interactionvoicefile.INTERACTIONVOICEFILE).where(Interactionvoicefile.INTERACTIONVOICEFILE.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
        }
        return null;
    }

    public Result<InteractiontriggersRecord> getTriggers(InteractionRecord interaction){
        if(Karren.conf.getAllowSQLRW()){
            refreshSQLConnection();
            return sqlConn.selectFrom(Interactiontriggers.INTERACTIONTRIGGERS).where(Interactiontriggers.INTERACTIONTRIGGERS.INTERACTIONID.eq(interaction.getInteractionid())).fetch();
        }
        return null;
    }

    public void addInteraction(IGuild guild, org.frostbite.karren.interactions.Interaction interaction){
        InteractionRecord dbInteration = sqlConn.insertInto(Interaction.INTERACTION).values(null, interaction.getIdentifier(), interaction.getHelptext(), interaction.getConfidence(), interaction.getPermissionLevel(), interaction.getChannel(), interaction.getParameter(), interaction.getVoiceVolume(), interaction.isSpecialInteraction(), interaction.isEnabled(), guild.getStringID()).returning().fetchOne();
        for(String template : interaction.getTemplates()) {
            InteractiontemplateRecord dbTemplate = sqlConn.insertInto(Interactiontemplate.INTERACTIONTEMPLATE).values(null, template).returning().fetchOne();
            sqlConn.insertInto(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE).values(null, dbInteration.getInteractionid(), dbTemplate.getTemplateid()).onDuplicateKeyIgnore().execute();
        }
        if(interaction.getTemplatesFail()!=null) {
            for (String template : interaction.getTemplatesFail()) {
                InteractiontemplateRecord dbTemplate = sqlConn.insertInto(Interactiontemplate.INTERACTIONTEMPLATE).values(null, template).returning().fetchOne();
                sqlConn.insertInto(Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE).values(null, dbInteration.getInteractionid(), dbTemplate.getTemplateid()).onDuplicateKeyIgnore().execute();
            }
        }
        if(interaction.getTemplatesPermError()!=null) {
            for (String template : interaction.getTemplatesPermError()) {
                InteractiontemplateRecord dbTemplate = sqlConn.insertInto(Interactiontemplate.INTERACTIONTEMPLATE).values(null, template).returning().fetchOne();
                sqlConn.insertInto(Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE).values(null, dbInteration.getInteractionid(), dbTemplate.getTemplateid()).onDuplicateKeyIgnore().execute();
            }
        }
        if(interaction.getTags()!=null) {
            for (String tag : interaction.getTags()) {
                InteractiontagRecord dbTag = sqlConn.selectFrom(Interactiontag.INTERACTIONTAG).where(Interactiontag.INTERACTIONTAG.TAG.equalIgnoreCase(tag)).fetchOne();
                if (dbTag != null) {
                    sqlConn.insertInto(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION).values(dbTag.getTagid(), dbInteration.getInteractionid()).onDuplicateKeyIgnore().execute();
                } else {
                    Karren.log.error("Invalid tag in default interaction, ignoring!");
                }
            }
        }
        for(String trigger : interaction.getTriggers()){
            sqlConn.insertInto(Interactiontriggers.INTERACTIONTRIGGERS).values(null, trigger, dbInteration.getInteractionid()).execute();
        }
    }

    public void addTag(String tag){
        sqlConn.insertInto(Interactiontag.INTERACTIONTAG).values(null, tag).onDuplicateKeyIgnore().execute();
    }

    public WordcountsRecord getWordCount(String word){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(Wordcounts.WORDCOUNTS).values(null, word, 1, null).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(Wordcounts.WORDCOUNTS).where(Wordcounts.WORDCOUNTS.WORD.equalIgnoreCase(word)).fetchOne();
        }
        return null;
    }

    public UserRecord getUserData(IUser user){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            sqlConn.insertInto(User.USER).values(user.getStringID(), null, user.getName(), null, 0, null).onDuplicateKeyIgnore().execute();
            return sqlConn.selectFrom(User.USER).where(User.USER.USERID.equalIgnoreCase(user.getStringID())).fetchOne();
        }
        return null;
    }

    public Result<FavoritesRecord> getUserFaves(int songid){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            return sqlConn.selectFrom(Favorites.FAVORITES).where(Favorites.FAVORITES.SONGID.equal(songid)).fetch();
        }
        return null;
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
        }
        return null;
    }

    public void updateDJActivity(String curDJ, String streamName){
        if(Karren.conf.getAllowSQLRW()) {
            refreshSQLConnection();
            //Switch off all DJ active statuses
            sqlConn.update(User.USER).set(User.USER.DJACTIVE, 0).execute();
            IUser newDJ = null;
            try {
                newDJ = Karren.bot.getClient().getUserByID(Long.getLong(curDJ));
            } catch (NullPointerException ignored){}
            if (newDJ != null) {
                UserRecord userAccount = getUserData(newDJ);
                userAccount.setDjactive(1);
                userAccount.setDjstreamname(streamName);
                userAccount.setDjname(newDJ.getName());
                userAccount.update();
            } else {
                sqlConn.insertInto(User.USER).values(0, null, curDJ, "default", 1, "streamName", 0).onDuplicateKeyUpdate().set(User.USER.DJNAME, curDJ).set(User.USER.DJSTREAMNAME, streamName).set(User.USER.DJACTIVE, 1).execute();
            }
        }
    }
}
