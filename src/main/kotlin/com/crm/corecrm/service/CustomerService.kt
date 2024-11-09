package com.crm.corecrm.service

import com.crm.corecrm.domain.model.Customer
import com.crm.corecrm.domain.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun getCustomersByIds(customerIds: List<Int>) : Map<Int, List<Customer>> {
        return customerRepository.getCustomersByIds(customerIds)
    }

    fun getIdOrCreate(customer: Customer) : Int {
        return customerRepository.getIdOrCreate(customer)
    }
}