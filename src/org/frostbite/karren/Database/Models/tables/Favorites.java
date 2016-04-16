/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.3.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Favorites extends org.jooq.impl.TableImpl<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord> {

	/**
	 * The singleton instance of <code>KarrenDB.Favorites</code>
	 */
	public static final org.frostbite.karren.Database.Models.tables.Favorites FAVORITES = new org.frostbite.karren.Database.Models.tables.Favorites();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord> getRecordType() {
		return org.frostbite.karren.Database.Models.tables.records.FavoritesRecord.class;
	}

	/**
	 * The column <code>KarrenDB.Favorites.FaveID</code>.
	 */
	public final org.jooq.TableField<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, java.lang.Integer> FAVEID = createField("FaveID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>KarrenDB.Favorites.SongID</code>.
	 */
	public final org.jooq.TableField<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, java.lang.Integer> SONGID = createField("SongID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>KarrenDB.Favorites.UserID</code>.
	 */
	public final org.jooq.TableField<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, java.lang.Long> USERID = createField("UserID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * Create a <code>KarrenDB.Favorites</code> table reference
	 */
	public Favorites() {
		this("Favorites", null);
	}

	/**
	 * Create an aliased <code>KarrenDB.Favorites</code> table reference
	 */
	public Favorites(java.lang.String alias) {
		this(alias, org.frostbite.karren.Database.Models.tables.Favorites.FAVORITES);
	}

	private Favorites(java.lang.String alias, org.jooq.Table<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord> aliased) {
		this(alias, aliased, null);
	}

	private Favorites(java.lang.String alias, org.jooq.Table<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.frostbite.karren.Database.Models.Karrendb.KARRENDB, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, java.lang.Integer> getIdentity() {
		return org.frostbite.karren.Database.Models.Keys.IDENTITY_FAVORITES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord> getPrimaryKey() {
		return org.frostbite.karren.Database.Models.Keys.KEY_FAVORITES_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord>>asList(org.frostbite.karren.Database.Models.Keys.KEY_FAVORITES_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.frostbite.karren.Database.Models.tables.records.FavoritesRecord, ?>>asList(org.frostbite.karren.Database.Models.Keys.FK_FAVORITES_SONGDB, org.frostbite.karren.Database.Models.Keys.FK_FAVORITES_USER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.frostbite.karren.Database.Models.tables.Favorites as(java.lang.String alias) {
		return new org.frostbite.karren.Database.Models.tables.Favorites(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.frostbite.karren.Database.Models.tables.Favorites rename(java.lang.String name) {
		return new org.frostbite.karren.Database.Models.tables.Favorites(name, null);
	}
}
