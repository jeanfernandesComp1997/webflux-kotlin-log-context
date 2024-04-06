package com.example.loggingcontextsample.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CharacterService(
    private val client: WebClient
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun retrieveCharacter(id: String): Any {
        logger.info("Requesting character on: '/character/$id'")


        val character = Mono.fromCallable {
            callBlocking(id)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingle()


        logger.info("Character response: $character")

        return character!!
    }

    private suspend fun callApi(id: String): Any {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .bodyToMono<Any>()
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingle()
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

//    fun retrieveCharacter(id: String): Mono<Any> {
//        logger.info("Requesting character on: '/character/$id'")
//
//        return client
//            .get()
//            .uri("/character/{id}", id)
//            .retrieve()
//            .bodyToMono<Any>()
//            .doOnSuccess { logger.info("Character response: $it") }
//            .subscribeOn(Schedulers.boundedElastic())
//    }
}