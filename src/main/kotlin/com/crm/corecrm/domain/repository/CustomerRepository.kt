package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Customer.CUSTOMER
import com.crm.corecrm.domain.db.tables.records.CustomerRecord
import com.crm.corecrm.domain.model.ChannelType
import com.crm.corecrm.domain.model.Customer
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class CustomerRepository(dsl: DSLContext) : AbstractRepository(dsl) {

    fun getByCustomer(customer: Customer): Customer? {
        val record = db.selectFrom(CUSTOMER)
            .where(CUSTOMER.CHANNEL_ID.eq(customer.channelId))
            .and(CUSTOMER.CHANNEL_TYPE.eq(customer.channelType.name))
            .fetchOne() ?: run { return null }
        return record.toCustomer()
    }

    fun create(customer: Customer): Customer {
        val record = db.newRecord(CUSTOMER).apply {
            channelId = customer.channelId
            channelType = customer.channelType.name
            firstname = customer.firstName
            lastname = customer.lastName
            username = customer.userName
        }
        record.store()
        return record.toCustomer()
    }

    fun getCustomersByIds(customerIds: List<Int>): Map<Int, Customer> {
        return db
            .selectFrom(CUSTOMER)
            .where(CUSTOMER.ID.`in`(customerIds))
            .fetch()
            .map { it.toCustomer() }
            .associateBy { it.id!! }
    }

    fun CustomerRecord.toCustomer(): Customer {
        return Customer(
            id = this.id,
            channelId = this.channelId,
            channelType = ChannelType.valueOf(this.channelType),
            firstName = this.firstname,
            lastName = this.lastname,
            userName = this.username
        )
    }
}