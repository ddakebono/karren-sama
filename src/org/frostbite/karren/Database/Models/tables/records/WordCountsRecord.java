/**
 * This class is generated by jOOQ
 */
package org.frostbite.karren.Database.Models.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.3.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WordCountsRecord extends org.jooq.impl.UpdatableRecordImpl<org.frostbite.karren.Database.Models.tables.records.WordCountsRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.String, java.lang.Long, java.sql.Timestamp> {

	/**
	 * Setter for <code>database.word_counts.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>database.word_counts.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>database.word_counts.word</code>.
	 */
	public void setWord(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>database.word_counts.word</code>.
	 */
	public java.lang.String getWord() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>database.word_counts.count</code>.
	 */
	public void setCount(java.lang.Long value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>database.word_counts.count</code>.
	 */
	public java.lang.Long getCount() {
		return (java.lang.Long) getValue(2);
	}

	/**
	 * Setter for <code>database.word_counts.count_started</code>.
	 */
	public void setCountStarted(java.sql.Timestamp value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>database.word_counts.count_started</code>.
	 */
	public java.sql.Timestamp getCountStarted() {
		return (java.sql.Timestamp) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Long, java.sql.Timestamp> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Long, java.sql.Timestamp> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS.WORD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field3() {
		return org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS.COUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field4() {
		return org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS.COUNT_STARTED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getWord();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value3() {
		return getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value4() {
		return getCountStarted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordCountsRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordCountsRecord value2(java.lang.String value) {
		setWord(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordCountsRecord value3(java.lang.Long value) {
		setCount(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordCountsRecord value4(java.sql.Timestamp value) {
		setCountStarted(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WordCountsRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.Long value3, java.sql.Timestamp value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached WordCountsRecord
	 */
	public WordCountsRecord() {
		super(org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS);
	}

	/**
	 * Create a detached, initialised WordCountsRecord
	 */
	public WordCountsRecord(java.lang.Integer id, java.lang.String word, java.lang.Long count, java.sql.Timestamp countStarted) {
		super(org.frostbite.karren.Database.Models.tables.WordCounts.WORD_COUNTS);

		setValue(0, id);
		setValue(1, word);
		setValue(2, count);
		setValue(3, countStarted);
	}
}
