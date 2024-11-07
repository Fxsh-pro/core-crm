package com.crm.corecrm.domain.repository

import com.crm.corecrm.domain.model.Customer
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class CustomerRepository(dsl : DSLContext) : AbstractRepository(dsl) {


    fun saveIfNotExists(customer: Customer) {
        // db.insertInto(Customer)
    }
}