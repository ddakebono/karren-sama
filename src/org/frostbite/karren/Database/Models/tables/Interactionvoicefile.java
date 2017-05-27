/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.Karrendb;
import org.frostbite.karren.Database.Models.Keys;
import org.frostbite.karren.Database.Models.tables.records.InteractionvoicefileRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.4"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Interactionvoicefile extends TableImpl<InteractionvoicefileRecord> {

	private static final long serialVersionUID = -558484862;

	/**
	 * The reference instance of <code>KarrenDB.InteractionVoiceFile</code>
	 */
	public static final Interactionvoicefile INTERACTIONVOICEFILE = new Interactionvoicefile();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<InteractionvoicefileRecord> getRecordType() {
		return InteractionvoicefileRecord.class;
	}

	/**
	 * The column <code>KarrenDB.InteractionVoiceFile.VoiceFileID</code>.
	 */
	public final TableField<InteractionvoicefileRecord, Integer> VOICEFILEID = createField("VoiceFileID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>KarrenDB.InteractionVoiceFile.VoiceFileLocation</code>.
	 */
	public final TableField<InteractionvoicefileRecord, String> VOICEFILELOCATION = createField("VoiceFileLocation", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>KarrenDB.InteractionVoiceFile.InteractionID</code>.
	 */
	public final TableField<InteractionvoicefileRecord, Integer> INTERACTIONID = createField("InteractionID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>KarrenDB.InteractionVoiceFile</code> table reference
	 */
	public Interactionvoicefile() {
		this("InteractionVoiceFile", null);
	}

	/**
	 * Create an aliased <code>KarrenDB.InteractionVoiceFile</code> table reference
	 */
	public Interactionvoicefile(String alias) {
		this(alias, INTERACTIONVOICEFILE);
	}

	private Interactionvoicefile(String alias, Table<InteractionvoicefileRecord> aliased) {
		this(alias, aliased, null);
	}

	private Interactionvoicefile(String alias, Table<InteractionvoicefileRecord> aliased, Field<?>[] parameters) {
		super(alias, Karrendb.KARRENDB, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<InteractionvoicefileRecord, Integer> getIdentity() {
		return Keys.IDENTITY_INTERACTIONVOICEFILE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<InteractionvoicefileRecord> getPrimaryKey() {
		return Keys.KEY_INTERACTIONVOICEFILE_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<InteractionvoicefileRecord>> getKeys() {
		return Arrays.<UniqueKey<InteractionvoicefileRecord>>asList(Keys.KEY_INTERACTIONVOICEFILE_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<InteractionvoicefileRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<InteractionvoicefileRecord, ?>>asList(Keys.FK_INTERACTIONVOICEFILE_INTERACTION1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interactionvoicefile as(String alias) {
		return new Interactionvoicefile(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Interactionvoicefile rename(String name) {
		return new Interactionvoicefile(name, null);
	}
}
