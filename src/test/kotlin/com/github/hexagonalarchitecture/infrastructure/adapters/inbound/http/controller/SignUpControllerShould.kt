package com.github.hexagonalarchitecture.infrastructure.adapters.inbound.http.controller

import arrow.core.Either
import com.github.hexagonalarchitecture.application.service.SignUpCustomerRequest
import com.github.hexagonalarchitecture.application.service.SignUpCustomerResponse
import com.github.hexagonalarchitecture.application.service.SignUpCustomerService
import com.github.hexagonalarchitecture.domain.model.DomainError
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.mockk.every
import java.util.UUID
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.kotlin.core.publisher.toMono

private val CUSTOMER_ID = UUID.randomUUID()

@Tag("integration")
@WebFluxTest(controllers = [SignUpController::class])
class SignUpControllerShould {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var signUpCustomerService: SignUpCustomerService

    @Test
    fun `sign up a customer`() {
        val signUpCustomerRequest = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org",
        )
        every {
            signUpCustomerService(signUpCustomerRequest)
        } returns Either.Right(SignUpCustomerResponse(id = CUSTOMER_ID))

        val response = webTestClient
            .post()
            .uri("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
                {
                    "id": "$CUSTOMER_ID",
                    "name": "Fabrizio",
                    "surname": "Di Napoli",
                    "email": "some.email@example.org"
                }
            """.trimIndent())
            .exchange()

        response
            .expectStatus()
            .isCreated
            .expectBody<String>()
            .returnResult().responseBodyContent!!.decodeToString().shouldBe("""
                {
                    "id": "$CUSTOMER_ID"
                }
            """.trimIndent())
    }

    @Test
    fun `return a 500 if any error in the use case`() {
        val signUpCustomerRequest = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org",
        )
        every { signUpCustomerService(signUpCustomerRequest) } returns Either.Left(DomainError("Any error"))

        val response = webTestClient
            .post()
            .uri("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
                {
                    "id": "$CUSTOMER_ID",
                    "name": "Fabrizio",
                    "surname": "Di Napoli",
                    "email": "some.email@example.org"
                }
            """.trimIndent())
            .exchange()

        response
            .expectStatus()
            .is5xxServerError
    }
}
