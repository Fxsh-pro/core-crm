/*
 * This file is generated by jOOQ.
*/
package com.crm.corecrm.domain.db.tables;


import com.crm.corecrm.domain.db.Indexes;
import com.crm.corecrm.domain.db.Keys;
import com.crm.corecrm.domain.db.Public;
import com.crm.corecrm.domain.db.tables.records.OperatorRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Operator extends TableImpl<OperatorRecord> {

    private static final long serialVersionUID = -1694052248;

    /**
     * The reference instance of <code>PUBLIC.OPERATOR</code>
     */
    public static final Operator OPERATOR = new Operator();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OperatorRecord> getRecordType() {
        return OperatorRecord.class;
    }

    /**
     * The column <code>PUBLIC.OPERATOR.ID</code>.
     */
    public final TableField<OperatorRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.OPERATOR.LOGIN</code>.
     */
    public final TableField<OperatorRecord, String> LOGIN = createField("LOGIN", org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.OPERATOR.PASSWORD</code>.
     */
    public final TableField<OperatorRecord, String> PASSWORD = createField("PASSWORD", org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.OPERATOR.NAME</code>.
     */
    public final TableField<OperatorRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * Create a <code>PUBLIC.OPERATOR</code> table reference
     */
    public Operator() {
        this(DSL.name("OPERATOR"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.OPERATOR</code> table reference
     */
    public Operator(String alias) {
        this(DSL.name(alias), OPERATOR);
    }

    /**
     * Create an aliased <code>PUBLIC.OPERATOR</code> table reference
     */
    public Operator(Name alias) {
        this(alias, OPERATOR);
    }

    private Operator(Name alias, Table<OperatorRecord> aliased) {
        this(alias, aliased, null);
    }

    private Operator(Name alias, Table<OperatorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CONSTRAINT_INDEX_1, Indexes.PRIMARY_KEY_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<OperatorRecord, Integer> getIdentity() {
        return Keys.IDENTITY_OPERATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<OperatorRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<OperatorRecord>> getKeys() {
        return Arrays.<UniqueKey<OperatorRecord>>asList(Keys.CONSTRAINT_1, Keys.CONSTRAINT_10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator as(String alias) {
        return new Operator(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operator as(Name alias) {
        return new Operator(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Operator rename(String name) {
        return new Operator(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Operator rename(Name name) {
        return new Operator(name, null);
    }
}