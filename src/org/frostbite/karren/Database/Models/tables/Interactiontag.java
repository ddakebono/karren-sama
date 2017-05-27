/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.Karrendb;
import org.frostbite.karren.Database.Models.Keys;
import org.frostbite.karren.Database.Models.tables.records.InteractiontagRecord;
import org.jooq.Field;
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
public class Interactiontag extends TableImpl<InteractiontagRecord> {

	private static final long serialVersionUID = 1967769531;

	/**
	 * The reference instance of <code>KarrenDB.InteractionTag</code>
	 */
	public static final Interactiontag INTERACTIONTAG = new Interactiontag();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<InteractiontagRecord> getRecordType() {
		return InteractiontagRecord.class;
	}

	/**
	 * The column <code>KarrenDB.InteractionTag.TagID</code>.
	 */
	public final TableField<InteractiontagRecord, Integer> TAGID = createField("TagID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>KarrenDB.InteractionTag.Tag</code>.
	 */
	public final TableField<InteractiontagRecord, String> TAG = createField("Tag", org.jooq.impl.SQLDataType.VARCHAR.length(100).nullable(false), this, "");

	/**
	 * Create a <code>KarrenDB.InteractionTag</code> table reference
	 */
	public Interactiontag() {
		this("InteractionTag", null);
	}

	/**
	 * Create an aliased <code>KarrenDB.InteractionTag</code> table reference
	 */
	public Interactiontag(String alias) {
		this(alias, INTERACTIONTAG);
	}

	private Interactiontag(String alias, Table<InteractiontagRecord> aliased) {
		this(alias, aliased, null);
	}

	private Interactiontag(String alias, Table<InteractiontagRecord> aliased, Field<?>[] parameters) {
		super(alias, Karrendb.KARRENDB, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<InteractiontagRecord, Integer> getIdentity() {
		return Keys.IDENTITY_INTERACTIONTAG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<InteractiontagRecord> getPrimaryKey() {
		return Keys.KEY_INTERACTIONTAG_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<InteractiontagRecord>> getKeys() {
		return Arrays.<UniqueKey<InteractiontagRecord>>asList(Keys.KEY_INTERACTIONTAG_PRIMARY, Keys.KEY_INTERACTIONTAG_INTERACTIONTAGCOL_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interactiontag as(String alias) {
		return new Interactiontag(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Interactiontag rename(String name) {
		return new Interactiontag(name, null);
	}
}
