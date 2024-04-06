package com.example.loggingcontextsample.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CharacterService(
    private val client: WebClient
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun retrieveCharacter(id: String): Any = coroutineScope {
        val character = async {
            logger.info("Requesting character on: '/character/$id'")
            Mono.fromCallable {
                callBlocking(id)
            }
                .subscribeOn(Schedulers.boundedElastic())
                .awaitSingleOrNull()
        }

        val character2 = async {
            logger.info("Requesting character2 on: '/character/$id'")
            callApi(id)
        }

        logger.info("Character response: ${character.await()}")
        logger.info("Character2 response: ${character2.await()}")

        return@coroutineScope character.await() ?: Any()
    }

    private suspend fun callApi(id: String): Any {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .awaitBody()
    }

    private fun callBlocking(id: String): Any? {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .bodyToMono<Any>()
            .subscribeOn(Schedulers.boundedElastic())
            .block()
    }

    fun retrieveCharacterWebFlux(id: String): Mono<Any> {
        logger.info("Requesting character with webflux on: '/character/$id'")

        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .bodyToMono<Any>()
            .doOnSuccess { logger.info("Character response with webflux: $it") }
            .subscribeOn(Schedulers.boundedElastic())
    }
}