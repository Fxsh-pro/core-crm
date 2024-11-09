package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.db.tables.Customer.CUSTOMER
import com.crm.corecrm.domain.db.tables.records.CustomerRecord
import com.crm.corecrm.domain.model.Customer
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class CustomerRepository(dsl: DSLContext) : AbstractRepository(dsl) {

    fun getIdOrCreate(customer: Customer): Int {
        val existingCustomerId = db.select(CUSTOMER.ID)
            .from(CUSTOMER)
            .where(CUSTOMER.TG_ID.eq(customer.tgId))
            .fetchOneInto(Int::class.java)

        return if (existingCustomerId != null) {
            existingCustomerId
        } else {
            val result = db.insertInto(CUSTOMER)
                .set(CUSTOMER.TG_ID, customer.tgId)
                .set(CUSTOMER.FIRSTNAME, customer.firstName)
                .set(CUSTOMER.LASTNAME, customer.lastName)
                .set(CUSTOMER.USERNAME, customer.userName)
                .returning(CUSTOMER.ID)
                .fetchOne()

            result?.getValue(CUSTOMER.ID)?.toInt() ?: throw IllegalStateException("Failed to create new customer")
        }
    }

    fun getCustomersByIds(customerIds: List<Int>): Map<Int, List<Customer>> {
        return db
            .selectFrom(CUSTOMER)
            .where(CUSTOMER.ID.`in`(customerIds))
            .fetch()
            .map { it.toCustomer() }
            .groupBy { it.id!! }
    }

    fun CustomerRecord.toCustomer(): Customer {
        return Customer(
            id = this.id,
            tgId = this.tgId,
            firstName = this.firstname,
            lastName = this.lastname,
            userName = this.username
        )
    }
}