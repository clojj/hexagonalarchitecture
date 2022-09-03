package com.github.hexagonalarchitecture.application.service

import arrow.core.Either
import com.github.hexagonalarchitecture.domain.model.Customer
import com.github.hexagonalarchitecture.domain.model.CustomerRepository
import com.github.hexagonalarchitecture.domain.model.DomainError
import java.util.UUID

class SignUpCustomerService(private val repository: CustomerRepository) {
    operator fun invoke(request: SignUpCustomerRequest): Either<DomainError, SignUpCustomerResponse> {
        return Customer.create(
            request.id,
            request.name,
            request.surname,
            request.email,
        )
            .map {
                repository.save(it)
                SignUpCustomerResponse(it.id.id)
            }
    }
}

data class SignUpCustomerRequest(
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
)

data class SignUpCustomerResponse(val id: UUID)
