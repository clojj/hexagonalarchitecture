package com.github.hexagonalarchitecture.infrastructure.adapters.outbound.memory

import com.github.hexagonalarchitecture.domain.model.Customer
import com.github.hexagonalarchitecture.domain.model.CustomerRepository

class InMemoryCustomerRepository : CustomerRepository {

    private val customers = mutableListOf<Customer>()

    override fun save(customer: Customer) {
        customers.add(customer)
    }
}
