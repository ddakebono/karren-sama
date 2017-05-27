/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables.records;


import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.tables.InteractiontagHasInteraction;
import org.jooq.Field;
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
public class InteractiontagHasInteractionRecord extends UpdatableRecordImpl<InteractiontagHasInteractionRecord> implements Record2<Integer, Integer> {

	private static final long serialVersionUID = 1971752615;

	/**
	 * Setter for <code>KarrenDB.InteractionTag_has_Interaction.TagID</code>.
	 */
	public void setTagid(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionTag_has_Interaction.TagID</code>.
	 */
	public Integer getTagid() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>KarrenDB.InteractionTag_has_Interaction.InteractionID</code>.
	 */
	public void setInteractionid(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionTag_has_Interaction.InteractionID</code>.
	 */
	public Integer getInteractionid() {
		return (Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record2<Integer, Integer> key() {
		return (Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.TAGID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION.INTERACTIONID;
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
	public Integer value2() {
		return getInteractionid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagHasInteractionRecord value1(Integer value) {
		setTagid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagHasInteractionRecord value2(Integer value) {
		setInteractionid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractiontagHasInteractionRecord values(Integer value1, Integer value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached InteractiontagHasInteractionRecord
	 */
	public InteractiontagHasInteractionRecord() {
		super(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION);
	}

	/**
	 * Create a detached, initialised InteractiontagHasInteractionRecord
	 */
	public InteractiontagHasInteractionRecord(Integer tagid, Integer interactionid) {
		super(InteractiontagHasInteraction.INTERACTIONTAG_HAS_INTERACTION);

		setValue(0, tagid);
		setValue(1, interactionid);
	}
}
