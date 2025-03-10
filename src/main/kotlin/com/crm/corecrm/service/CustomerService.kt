package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun getCustomersByIds(customerIds: List<Int>) : Map<Int, Customer> {
        return customerRepository.getCustomersByIds(customerIds)
    }

    fun getOrCreate(customer: Customer) : Customer {
        val existingCustomer = customerRepository.getByCustomer(customer)
        if (existingCustomer != null) {
            return existingCustomer
        }
        return customerRepository.create(customer)
    }
}