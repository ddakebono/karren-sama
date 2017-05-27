/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models;


import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.tables.Favorites;
import org.frostbite.karren.Database.Models.tables.Guild;
import org.frostbite.karren.Database.Models.tables.Interaction;
import org.frostbite.karren.Database.Models.tables.Interactionfailuretemplate;
import org.frostbite.karren.Database.Models.tables.Interactionpermissionerrortemplate;
import org.frostbite.karren.Database.Models.tables.Interactionsuccesstemplate;
import org.frostbite.karren.Database.Models.tables.Interactiontag;
import org.frostbite.karren.Database.Models.tables.InteractiontagHasInteraction;
import org.frostbite.karren.Database.Models.tables.Interactiontemplate;
import org.frostbite.karren.Database.Models.tables.Interactiontriggers;
import org.frostbite.karren.Database.Models.tables.Interactionvoicefile;
import org.frostbite.karren.Database.Models.tables.Songdb;
import org.frostbite.karren.Database.Models.tables.User;
import org.frostbite.karren.Database.Models.tables.Wordcounts;
import org.frostbite.karren.Database.Models.tables.records.FavoritesRecord;
import org.frostbite.karren.Database.Models.tables.records.GuildRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractionRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractionfailuretemplateRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractionpermissionerrortemplateRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractionsuccesstemplateRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractiontagHasInteractionRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractiontagRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractiontemplateRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractiontriggersRecord;
import org.frostbite.karren.Database.Models.tables.records.InteractionvoicefileRecord;
import org.frostbite.karren.Database.Models.tables.records.SongdbRecord;
import org.frostbite.karren.Database.Models.tables.records.UserRecord;
import org.frostbite.karren.Database.Models.tables.records.WordcountsRecord;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>KarrenDB</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.4"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<FavoritesRecord, Integer> IDENTITY_FAVORITES = Identities0.IDENTITY_FAVORITES;
	public static final Identity<InteractionRecord, Integer> IDENTITY_INTERACTION = Identities0.IDENTITY_INTERACTION;
	public static final Identity<InteractionfailuretemplateRecord, Integer> IDENTITY_INTERACTIONFAILURETEMPLATE = Identities0.IDENTITY_INTERACTIONFAILURETEMPLATE;
	public static final Identity<InteractionpermissionerrortemplateRecord, Integer> IDENTITY_INTERACTIONPERMISSIONERRORTEMPLATE = Identities0.IDENTITY_INTERACTIONPERMISSIONERRORTEMPLATE;
	public static final Identity<InteractionsuccesstemplateRecord, Integer> IDENTITY_INTERACTIONSUCCESSTEMPLATE = Identities0.IDENTITY_INTERACTIONSUCCESSTEMPLATE;
	public static final Identity<InteractiontagRecord, Integer> IDENTITY_INTERACTIONTAG = Identities0.IDENTITY_INTERACTIONTAG;
	public static final Identity<InteractiontemplateRecord, Integer> IDENTITY_INTERACTIONTEMPLATE = Identities0.IDENTITY_INTERACTIONTEMPLATE;
	public static final Identity<InteractiontriggersRecord, Integer> IDENTITY_INTERACTIONTRIGGERS = Identities0.IDENTITY_INTERACTIONTRIGGERS;
	public static final Identity<InteractionvoicefileRecord, Integer> IDENTITY_INTERACTIONVOICEFILE = Identities0.IDENTITY_INTERACTIONVOICEFILE;
	public static final Identity<SongdbRecord, Integer> IDENTITY_SONGDB = Identities0.IDENTITY_SONGDB;
	public static final Identity<WordcountsRecord, Integer> IDENTITY_WORDCOUNTS = Identities0.IDENTITY_WORDCOUNTS;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<FavoritesRecord> KEY_FAVORITES_PRIMARY = UniqueKeys0.KEY_FAVORITES_PRIMARY;
	public static final UniqueKey<GuildRecord> KEY_GUILD_PRIMARY = UniqueKeys0.KEY_GUILD_PRIMARY;
	public static final UniqueKey<InteractionRecord> KEY_INTERACTION_PRIMARY = UniqueKeys0.KEY_INTERACTION_PRIMARY;
	public static final UniqueKey<InteractionRecord> KEY_INTERACTION_IDENTIFIER_UNIQUE = UniqueKeys0.KEY_INTERACTION_IDENTIFIER_UNIQUE;
	public static final UniqueKey<InteractionfailuretemplateRecord> KEY_INTERACTIONFAILURETEMPLATE_PRIMARY = UniqueKeys0.KEY_INTERACTIONFAILURETEMPLATE_PRIMARY;
	public static final UniqueKey<InteractionpermissionerrortemplateRecord> KEY_INTERACTIONPERMISSIONERRORTEMPLATE_PRIMARY = UniqueKeys0.KEY_INTERACTIONPERMISSIONERRORTEMPLATE_PRIMARY;
	public static final UniqueKey<InteractionsuccesstemplateRecord> KEY_INTERACTIONSUCCESSTEMPLATE_PRIMARY = UniqueKeys0.KEY_INTERACTIONSUCCESSTEMPLATE_PRIMARY;
	public static final UniqueKey<InteractiontagRecord> KEY_INTERACTIONTAG_PRIMARY = UniqueKeys0.KEY_INTERACTIONTAG_PRIMARY;
	public static final UniqueKey<InteractiontagRecord> KEY_INTERACTIONTAG_INTERACTIONTAGCOL_UNIQUE = UniqueKeys0.KEY_INTERACTIONTAG_INTERACTIONTAGCOL_UNIQUE;
	public static final UniqueKey<InteractiontagHasInteractionRecord> KEY_INTERACTIONTAG_HAS_INTERACTION_PRIMARY = UniqueKeys0.KEY_INTERACTIONTAG_HAS_INTERACTION_PRIMARY;
	public static final UniqueKey<InteractiontemplateRecord> KEY_INTERACTIONTEMPLATE_PRIMARY = UniqueKeys0.KEY_INTERACTIONTEMPLATE_PRIMARY;
	public static final UniqueKey<InteractiontriggersRecord> KEY_INTERACTIONTRIGGERS_PRIMARY = UniqueKeys0.KEY_INTERACTIONTRIGGERS_PRIMARY;
	public static final UniqueKey<InteractionvoicefileRecord> KEY_INTERACTIONVOICEFILE_PRIMARY = UniqueKeys0.KEY_INTERACTIONVOICEFILE_PRIMARY;
	public static final UniqueKey<SongdbRecord> KEY_SONGDB_PRIMARY = UniqueKeys0.KEY_SONGDB_PRIMARY;
	public static final UniqueKey<SongdbRecord> KEY_SONGDB_SONGTITLE = UniqueKeys0.KEY_SONGDB_SONGTITLE;
	public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = UniqueKeys0.KEY_USER_PRIMARY;
	public static final UniqueKey<WordcountsRecord> KEY_WORDCOUNTS_PRIMARY = UniqueKeys0.KEY_WORDCOUNTS_PRIMARY;
	public static final UniqueKey<WordcountsRecord> KEY_WORDCOUNTS_WORD = UniqueKeys0.KEY_WORDCOUNTS_WORD;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<FavoritesRecord, SongdbRecord> FK_FAVORITES_SONGDB = ForeignKeys0.FK_FAVORITES_SONGDB;
	public static final ForeignKey<FavoritesRecord, UserRecord> FK_FAVORITES_USER = ForeignKeys0.FK_FAVORITES_USER;
	public static final ForeignKey<InteractionRecord, GuildRecord> FK_INTERACTION_GUILD1 = ForeignKeys0.FK_INTERACTION_GUILD1;
	public static final ForeignKey<InteractionfailuretemplateRecord, InteractionRecord> FK_INTERACTIONFAILURETEMPLATE_INTERACTION1 = ForeignKeys0.FK_INTERACTIONFAILURETEMPLATE_INTERACTION1;
	public static final ForeignKey<InteractionfailuretemplateRecord, InteractiontemplateRecord> FK_INTERACTIONFAILURETEMPLATE_INTERACTIONTEMPLATE1 = ForeignKeys0.FK_INTERACTIONFAILURETEMPLATE_INTERACTIONTEMPLATE1;
	public static final ForeignKey<InteractionpermissionerrortemplateRecord, InteractionRecord> FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTION1 = ForeignKeys0.FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTION1;
	public static final ForeignKey<InteractionpermissionerrortemplateRecord, InteractiontemplateRecord> FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTIONTEMPLATE1 = ForeignKeys0.FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTIONTEMPLATE1;
	public static final ForeignKey<InteractionsuccesstemplateRecord, InteractionRecord> FK_INTERACTIONSUCCESSTEMPLATE_INTERACTION1 = ForeignKeys0.FK_INTERACTIONSUCCESSTEMPLATE_INTERACTION1;
	public static final ForeignKey<InteractionsuccesstemplateRecord, InteractiontemplateRecord> FK_INTERACTIONSUCCESSTEMPLATE_INTERACTIONTEMPLATE1 = ForeignKeys0.FK_INTERACTIONSUCCESSTEMPLATE_INTERACTIONTEMPLATE1;
	public static final ForeignKey<InteractiontagHasInteractionRecord, InteractiontagRecord> FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTIONTAG1 = ForeignKeys0.FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTIONTAG1;
	public static final ForeignKey<InteractiontagHasInteractionRecord, InteractionRecord> FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTION1 = ForeignKeys0.FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTION1;
	public static final ForeignKey<InteractiontriggersRecord, InteractionRecord> FK_INTERACTIONTRIGGERS_INTERACTION1 = ForeignKeys0.FK_INTERACTIONTRIGGERS_INTERACTION1;
	public static final ForeignKey<InteractionvoicefileRecord, InteractionRecord> FK_INTERACTIONVOICEFILE_INTERACTION1 = ForeignKeys0.FK_INTERACTIONVOICEFILE_INTERACTION1;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<FavoritesRecord, Integer> IDENTITY_FAVORITES = createIdentity(Favorites.FAVORITES, Favorites.FAVORITES.FAVEID);
		public static Identity<InteractionRecord, Integer> IDENTITY_INTERACTION = createIdentity(Interaction.INTERACTION, Interaction.INTERACTION.INTERACTIONID);
		public static Identity<InteractionfailuretemplateRecord, Integer> IDENTITY_INTERACTIONFAILURETEMPLATE = createIdentity(Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.FAILURETEMPLATEID);
		public static Identity<InteractionpermissionerrortemplateRecord, Integer> IDENTITY_INTERACTIONPERMISSIONERRORTEMPLATE = createIdentity(Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.PERMERRORID);
		public static Identity<InteractionsuccesstemplateRecord, Integer> IDENTITY_INTERACTIONSUCCESSTEMPLATE = createIdentity(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.SUCCESSID);
		public static Identity<InteractiontagRecord, Integer> IDENTITY_INTERACTIONTAG = createIdentity(Interactiontag.INTERACTIONTAG, Interactiontag.INTERACTIONTAG.TAGID);
		public static Identity<InteractiontemplateRecord, Integer> IDENTITY_INTERACTIONTEMPLATE = createIdentity(Interactiontemplate.INTERACTIONTEMPLATE, Interactiontemplate.INTERACTIONTEMPLATE.TEMPLATEID);
		public static Identity<InteractiontriggersRecord, Integer> IDENTITY_INTERACTIONTRIGGERS = createIdentity(Interactiontriggers.INTERACTIONTRIGGERS, Interactiontriggers.INTERACTIONTRIGGERS.TRIGGERID);
		public static Identity<InteractionvoicefileRecord, Integer> IDENTITY_INTERACTIONVOICEFILE = createIdentity(Interactionvoicefile.INTERACTIONVOICEFILE, Interactionvoicefile.INTERACTIONVOICEFILE.VOICEFILEID);
		public static Identity<SongdbRecord, Integer> IDENTITY_SONGDB = createIdentity(Songdb.SONGDB, Songdb.SONGDB.ID);
		public static Identity<WordcountsRecord, Integer> IDENTITY_WORDCOUNTS = createIdentity(Wordcounts.WORDCOUNTS, Wordcounts.WORDCOUNTS.WORDID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<FavoritesRecord> KEY_FAVORITES_PRIMARY = createUniqueKey(Favorites.FAVORITES, Favorites.FAVORITES.FAVEID);
		public static final UniqueKey<GuildRecord> KEY_GUILD_PRIMARY = createUniqueKey(Guild.GUILD, Guild.GUILD.GUILDID);
		public static final UniqueKey<InteractionRecord> KEY_INTERACTION_PRIMARY = createUniqueKey(Interaction.INTERACTION, Interaction.INTERACTION.INTERACTIONID);
		public static final UniqueKey<InteractionRecord> KEY_INTERACTION_IDENTIFIER_UNIQUE = createUniqueKey(Interaction.INTERACTION, Interaction.INTERACTION.IDENTIFIER);
		public static final UniqueKey<InteractionfailuretemplateRecord> KEY_INTERACTIONFAILURETEMPLATE_PRIMARY = createUniqueKey(Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.FAILURETEMPLATEID, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.INTERACTIONID, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.TEMPLATEID);
		public static final UniqueKey<InteractionpermissionerrortemplateRecord> KEY_INTERACTIONPERMISSIONERRORTEMPLATE_PRIMARY = createUniqueKey(Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.PERMERRORID, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.INTERACTIONID, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.TEMPLATEID);
		public static final UniqueKey<InteractionsuccesstemplateRecord> KEY_INTERACTIONSUCCESSTEMPLATE_PRIMARY = createUniqueKey(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.SUCCESSID, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.INTERACTIONID, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.TEMPLATEID);
		public static final UniqueKey<InteractiontagRecord> KEY_INTERACTIONTAG_PRIMARY = createUniqueKey(Interactiontag.INTERACTIONTAG, Interactiontag.INTERACTIONTAG.TAGID);
		public static final UniqueKey<InteractiontagRecord> KEY_INTERACTIONTAG_INTERACTIONTAGCOL_UNIQUE = createUniqueKey(Interactiontag.INTERACTIONTAG, Interactiontag.INTERACTIONTAG.TAG);
		public static final UniqueKey<InteractiontagHasInteractionRecord> KEY_INTERACTIONTAG_HAS_INTERACTION_PRIMARY = createUniqueKey(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.TAGID, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.INTERACTIONID);
		public static final UniqueKey<InteractiontemplateRecord> KEY_INTERACTIONTEMPLATE_PRIMARY = createUniqueKey(Interactiontemplate.INTERACTIONTEMPLATE, Interactiontemplate.INTERACTIONTEMPLATE.TEMPLATEID);
		public static final UniqueKey<InteractiontriggersRecord> KEY_INTERACTIONTRIGGERS_PRIMARY = createUniqueKey(Interactiontriggers.INTERACTIONTRIGGERS, Interactiontriggers.INTERACTIONTRIGGERS.TRIGGERID);
		public static final UniqueKey<InteractionvoicefileRecord> KEY_INTERACTIONVOICEFILE_PRIMARY = createUniqueKey(Interactionvoicefile.INTERACTIONVOICEFILE, Interactionvoicefile.INTERACTIONVOICEFILE.VOICEFILEID);
		public static final UniqueKey<SongdbRecord> KEY_SONGDB_PRIMARY = createUniqueKey(Songdb.SONGDB, Songdb.SONGDB.ID);
		public static final UniqueKey<SongdbRecord> KEY_SONGDB_SONGTITLE = createUniqueKey(Songdb.SONGDB, Songdb.SONGDB.SONGTITLE);
		public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = createUniqueKey(User.USER, User.USER.USERID);
		public static final UniqueKey<WordcountsRecord> KEY_WORDCOUNTS_PRIMARY = createUniqueKey(Wordcounts.WORDCOUNTS, Wordcounts.WORDCOUNTS.WORDID);
		public static final UniqueKey<WordcountsRecord> KEY_WORDCOUNTS_WORD = createUniqueKey(Wordcounts.WORDCOUNTS, Wordcounts.WORDCOUNTS.WORD);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<FavoritesRecord, SongdbRecord> FK_FAVORITES_SONGDB = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_SONGDB_PRIMARY, Favorites.FAVORITES, Favorites.FAVORITES.SONGID);
		public static final ForeignKey<FavoritesRecord, UserRecord> FK_FAVORITES_USER = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_USER_PRIMARY, Favorites.FAVORITES, Favorites.FAVORITES.USERID);
		public static final ForeignKey<InteractionRecord, GuildRecord> FK_INTERACTION_GUILD1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_GUILD_PRIMARY, Interaction.INTERACTION, Interaction.INTERACTION.GUILDID);
		public static final ForeignKey<InteractionfailuretemplateRecord, InteractionRecord> FK_INTERACTIONFAILURETEMPLATE_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.INTERACTIONID);
		public static final ForeignKey<InteractionfailuretemplateRecord, InteractiontemplateRecord> FK_INTERACTIONFAILURETEMPLATE_INTERACTIONTEMPLATE1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTIONTEMPLATE_PRIMARY, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE, Interactionfailuretemplate.INTERACTIONFAILURETEMPLATE.TEMPLATEID);
		public static final ForeignKey<InteractionpermissionerrortemplateRecord, InteractionRecord> FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.INTERACTIONID);
		public static final ForeignKey<InteractionpermissionerrortemplateRecord, InteractiontemplateRecord> FK_INTERACTIONPERMISSIONERRORTEMPLATE_INTERACTIONTEMPLATE1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTIONTEMPLATE_PRIMARY, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE, Interactionpermissionerrortemplate.INTERACTIONPERMISSIONERRORTEMPLATE.TEMPLATEID);
		public static final ForeignKey<InteractionsuccesstemplateRecord, InteractionRecord> FK_INTERACTIONSUCCESSTEMPLATE_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.INTERACTIONID);
		public static final ForeignKey<InteractionsuccesstemplateRecord, InteractiontemplateRecord> FK_INTERACTIONSUCCESSTEMPLATE_INTERACTIONTEMPLATE1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTIONTEMPLATE_PRIMARY, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE, Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.TEMPLATEID);
		public static final ForeignKey<InteractiontagHasInteractionRecord, InteractiontagRecord> FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTIONTAG1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTIONTAG_PRIMARY, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.TAGID);
		public static final ForeignKey<InteractiontagHasInteractionRecord, InteractionRecord> FK_INTERACTIONTAG_HAS_INTERACTION_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION, InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.INTERACTIONID);
		public static final ForeignKey<InteractiontriggersRecord, InteractionRecord> FK_INTERACTIONTRIGGERS_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, Interactiontriggers.INTERACTIONTRIGGERS, Interactiontriggers.INTERACTIONTRIGGERS.INTERACTIONID);
		public static final ForeignKey<InteractionvoicefileRecord, InteractionRecord> FK_INTERACTIONVOICEFILE_INTERACTION1 = createForeignKey(org.frostbite.karren.Database.Models.Keys.KEY_INTERACTION_PRIMARY, Interactionvoicefile.INTERACTIONVOICEFILE, Interactionvoicefile.INTERACTIONVOICEFILE.INTERACTIONID);
	}
}
