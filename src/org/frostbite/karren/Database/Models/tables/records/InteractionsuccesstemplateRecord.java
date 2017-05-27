/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables.records;


import javax.annotation.Generated;

import org.frostbite.karren.Database.Models.tables.Interactionsuccesstemplate;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class InteractionsuccesstemplateRecord extends UpdatableRecordImpl<InteractionsuccesstemplateRecord> implements Record3<Integer, Integer, Integer> {

	private static final long serialVersionUID = 914824579;

	/**
	 * Setter for <code>KarrenDB.InteractionSuccessTemplate.SuccessID</code>.
	 */
	public void setSuccessid(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionSuccessTemplate.SuccessID</code>.
	 */
	public Integer getSuccessid() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>KarrenDB.InteractionSuccessTemplate.InteractionID</code>.
	 */
	public void setInteractionid(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionSuccessTemplate.InteractionID</code>.
	 */
	public Integer getInteractionid() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>KarrenDB.InteractionSuccessTemplate.TemplateID</code>.
	 */
	public void setTemplateid(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>KarrenDB.InteractionSuccessTemplate.TemplateID</code>.
	 */
	public Integer getTemplateid() {
		return (Integer) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record3<Integer, Integer, Integer> key() {
		return (Record3) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, Integer> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, Integer> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.SUCCESSID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.INTERACTIONID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE.TEMPLATEID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getSuccessid();
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
	public Integer value3() {
		return getTemplateid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractionsuccesstemplateRecord value1(Integer value) {
		setSuccessid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractionsuccesstemplateRecord value2(Integer value) {
		setInteractionid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractionsuccesstemplateRecord value3(Integer value) {
		setTemplateid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InteractionsuccesstemplateRecord values(Integer value1, Integer value2, Integer value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached InteractionsuccesstemplateRecord
	 */
	public InteractionsuccesstemplateRecord() {
		super(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE);
	}

	/**
	 * Create a detached, initialised InteractionsuccesstemplateRecord
	 */
	public InteractionsuccesstemplateRecord(Integer successid, Integer interactionid, Integer templateid) {
		super(Interactionsuccesstemplate.INTERACTIONSUCCESSTEMPLATE);

		setValue(0, successid);
		setValue(1, interactionid);
		setValue(2, templateid);
	}
}
