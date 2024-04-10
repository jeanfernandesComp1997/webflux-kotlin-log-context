package com.example.loggingcontextsample.controllers

import com.example.loggingcontextsample.dto.ResponseBody
import com.example.loggingcontextsample.services.CharacterService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("characters")
class CharacterController(
    private val characterService: CharacterService
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{id}")
    suspend fun retrieveCharacterById(@PathVariable id: String): ResponseEntity<ResponseBody> {
        logger.info("Request received, search character id: $id")
        val character = characterService.retrieveCharacter(id)

        val userKey = retrieveUserKey()

        logger.info("Building response")
        return ResponseEntity.ok(ResponseBody(userId = userKey, data = character))
    }

    private suspend fun retrieveUserKey(): String? {
        return Mono.deferContextual { context ->
            val key = context.getOrEmpty<List<String>>("userKey")
            Mono.just(if (key.get() is List) key.get()[0] else "")
        }.awaitSingleOrNull()
    }
}