package com.example.loggingcontextsample.services

import com.example.loggingcontextsample.utils.VT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CharacterService(
    private val client: WebClient,
    private val restTemplateClient: RestTemplate
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun retrieveCharacter(id: String): Any {
        logger.info("Requesting character on: '/character/$id'")
        val character = Mono.fromCallable {
            callBlocking(id)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingleOrNull()
        logger.info("Character response: $character")
        return character ?: Any()
    }

    suspend fun retrieveCharacterSuspend(id: String): Any {
        return callApi(id)
    }

    suspend fun retrieveCharacterVirtualThreads(id: String): Any = withContext(Dispatchers.VT) {
        logger.info("Virtual thread requesting character on: '/character/$id'")
        return@withContext callBlockingRestTemplate(id)
    }

    fun retrieveCharacterBlocking(id: String): Any? {
        return callBlockingVanilla(id)
    }

    suspend fun retrieveCharacterBoundedElastic(id: String): Any {
        logger.info("Bounded elastic requesting character on: '/character/$id'")
        val character = callBlockingRestTemplateBoundedElastic(id)
        logger.info("Bounded elastic character response: $character")
        return character
    }

    private suspend fun callApi(id: String): Any {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .awaitBody()
    }

    private suspend fun callBlockingRestTemplateBoundedElastic(id: String): Any {
        return Mono.fromCallable {
            callBlockingRestTemplate(id)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingle()
    }

    private fun callBlockingRestTemplate(id: String): Any {
        return restTemplateClient
            .getForEntity("/character/$id", Any::class.java)
    }

    private fun callBlocking(id: String): Any? {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .bodyToMono<Any>()
            .block()
    }

    private fun callBlockingVanilla(id: String): Any? {
        return client
            .get()
            .uri("/character/{id}", id)
            .retrieve()
            .bodyToMono<Any>()
            .block()
    }
}