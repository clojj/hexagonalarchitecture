package com.github.hexagonalarchitecture.domain.model

interface CustomerRepository {
    fun save(customer: Customer): Unit
}
