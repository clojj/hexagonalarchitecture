package com.github.hexagonalarchitecture.infrastructure.configuration

import com.github.hexagonalarchitecture.application.service.SignUpCustomerService
import com.github.hexagonalarchitecture.domain.model.CustomerRepository
import com.github.hexagonalarchitecture.infrastructure.adapters.outbound.postgres.PostgresCustomerRepository
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class ApplicationConfiguration {

    @Bean
    fun customerRepository(jdbcTemplate: JdbcTemplate) = PostgresCustomerRepository(jdbcTemplate, Clock.systemUTC())

    @Bean
    fun signUpCustomerService(repository: CustomerRepository) = SignUpCustomerService(repository)
}
