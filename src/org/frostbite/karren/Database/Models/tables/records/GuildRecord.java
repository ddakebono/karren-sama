/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables.records;


import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.tables.Guild;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


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
public class GuildRecord extends UpdatableRecordImpl<GuildRecord> implements Record5<String, String, String, String, Integer> {

	private static final long serialVersionUID = -838907201;

	/**
	 * Setter for <code>KarrenDB.Guild.GuildID</code>.
	 */
	public void setGuildid(String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>KarrenDB.Guild.GuildID</code>.
	 */
	public String getGuildid() {
		return (String) getValue(0);
	}

	/**
	 * Setter for <code>KarrenDB.Guild.GuildOwner</code>.
	 */
	public void setGuildowner(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>KarrenDB.Guild.GuildOwner</code>.
	 */
	public String getGuildowner() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>KarrenDB.Guild.GuildName</code>.
	 */
	public void setGuildname(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>KarrenDB.Guild.GuildName</code>.
	 */
	public String getGuildname() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>KarrenDB.Guild.CommandPrefix</code>.
	 */
	public void setCommandprefix(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>KarrenDB.Guild.CommandPrefix</code>.
	 */
	public String getCommandprefix() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>KarrenDB.Guild.RollDifficulty</code>.
	 */
	public void setRolldifficulty(Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>KarrenDB.Guild.RollDifficulty</code>.
	 */
	public Integer getRolldifficulty() {
		return (Integer) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<String> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<String, String, String, String, Integer> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<String, String, String, String, Integer> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field1() {
		return Guild.GUILD.GUILDID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Guild.GUILD.GUILDOWNER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return Guild.GUILD.GUILDNAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return Guild.GUILD.COMMANDPREFIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return Guild.GUILD.ROLLDIFFICULTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value1() {
		return getGuildid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getGuildowner();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getGuildname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getCommandprefix();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getRolldifficulty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord value1(String value) {
		setGuildid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord value2(String value) {
		setGuildowner(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord value3(String value) {
		setGuildname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord value4(String value) {
		setCommandprefix(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord value5(Integer value) {
		setRolldifficulty(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuildRecord values(String value1, String value2, String value3, String value4, Integer value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached GuildRecord
	 */
	public GuildRecord() {
		super(Guild.GUILD);
	}

	/**
	 * Create a detached, initialised GuildRecord
	 */
	public GuildRecord(String guildid, String guildowner, String guildname, String commandprefix, Integer rolldifficulty) {
		super(Guild.GUILD);

		setValue(0, guildid);
		setValue(1, guildowner);
		setValue(2, guildname);
		setValue(3, commandprefix);
		setValue(4, rolldifficulty);
	}
}
