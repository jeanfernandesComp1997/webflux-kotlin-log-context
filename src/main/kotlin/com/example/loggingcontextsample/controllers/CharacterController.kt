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
import java.util.*

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

    @GetMapping("{id}/suspend")
    suspend fun retrieveCharacterByIdSuspend(@PathVariable id: String): ResponseEntity<ResponseBody> {
        logger.info("Suspend request received, search character id: $id")
        val character = characterService.retrieveCharacterSuspend(id)
        logger.info("Building suspend response")
        return ResponseEntity.ok(ResponseBody(userId = UUID.randomUUID().toString(), data = character))
    }

    @GetMapping("{id}/virtual-threads")
    suspend fun retrieveCharacterByIdVirtualThreads(@PathVariable id: String): ResponseEntity<ResponseBody> {
        logger.info("Virtual Threads request received, search character id: $id")
        val character = characterService.retrieveCharacterVirtualThreads(id)
        logger.info("Building Virtual Threads response")
        return ResponseEntity.ok(ResponseBody(userId = UUID.randomUUID().toString(), data = character))
    }

    @GetMapping("{id}/blocking")
    suspend fun retrieveCharacterByIdBlocking(@PathVariable id: String): ResponseEntity<ResponseBody> {
        logger.info("Blocking request received, search character id: $id")
        val character = characterService.retrieveCharacterBlocking(id)
        logger.info("Blocking Threads response")
        return ResponseEntity.ok(ResponseBody(userId = UUID.randomUUID().toString(), data = character ?: ""))
    }

    private suspend fun retrieveUserKey(): String? {
        return Mono.deferContextual { context ->
            val key = context.getOrEmpty<List<String>>("userKey")
            Mono.just(if (key.get() is List) key.get()[0] else "")
        }.awaitSingleOrNull()
    }
}