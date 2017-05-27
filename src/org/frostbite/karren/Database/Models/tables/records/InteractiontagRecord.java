/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables.records;


import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.tables.Interactiontag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class InteractiontagRecord extends UpdatableRecordImpl<InteractiontagRecord> implements Record2<Integer, String> {

	private static final long serialVersionUID = -2039992496;

	/**
	 * Setter for <code>KarrenDB.InteractionTag.TagID</code>.
	 */
	public void setTagid(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionTag.TagID</code>.
	 */
	public Integer getTagid() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>KarrenDB.InteractionTag.Tag</code>.
	 */
	public void setTag(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionTag.Tag</code>.
	 */
	public String getTag() {
		return (String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Interactiontag.INTERACTIONTAG.TAGID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Interactiontag.INTERACTIONTAG.TAG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getTagid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getTag();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagRecord value1(Integer value) {
		setTagid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagRecord value2(String value) {
		setTag(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagRecord values(Integer value1, String value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached InteractiontagRecord
	 */
	public InteractiontagRecord() {
		super(Interactiontag.INTERACTIONTAG);
	}

	/**
	 * Create a detached, initialised InteractiontagRecord
	 */
	public InteractiontagRecord(Integer tagid, String tag) {
		super(Interactiontag.INTERACTIONTAG);

		setValue(0, tagid);
		setValue(1, tag);
	}
}
