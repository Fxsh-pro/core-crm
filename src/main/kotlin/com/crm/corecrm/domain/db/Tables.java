/*
 * This file is generated by jOOQ.
*/
package com.crm.corecrm.domain.db;


import com.crm.corecrm.domain.db.tables.Chat;
import com.crm.corecrm.domain.db.tables.Customer;
import com.crm.corecrm.domain.db.tables.Message;
import com.crm.corecrm.domain.db.tables.Operator;
import com.crm.corecrm.domain.db.tables.OperatorDialog;
import com.crm.corecrm.domain.db.tables.SchemaVersion;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in PUBLIC
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>PUBLIC.CHAT</code>.
     */
    public static final Chat CHAT = com.crm.corecrm.domain.db.tables.Chat.CHAT;

    /**
     * The table <code>PUBLIC.CUSTOMER</code>.
     */
    public static final Customer CUSTOMER = com.crm.corecrm.domain.db.tables.Customer.CUSTOMER;

    /**
     * The table <code>PUBLIC.MESSAGE</code>.
     */
    public static final Message MESSAGE = com.crm.corecrm.domain.db.tables.Message.MESSAGE;

    /**
     * The table <code>PUBLIC.OPERATOR</code>.
     */
    public static final Operator OPERATOR = com.crm.corecrm.domain.db.tables.Operator.OPERATOR;

    /**
     * The table <code>PUBLIC.OPERATOR_DIALOG</code>.
     */
    public static final OperatorDialog OPERATOR_DIALOG = com.crm.corecrm.domain.db.tables.OperatorDialog.OPERATOR_DIALOG;

    /**
     * The table <code>PUBLIC.schema_version</code>.
     */
    public static final SchemaVersion SCHEMA_VERSION = com.crm.corecrm.domain.db.tables.SchemaVersion.SCHEMA_VERSION;
}
