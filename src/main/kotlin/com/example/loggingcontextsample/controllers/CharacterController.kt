package com.example.loggingcontextsample.controllers

import com.example.loggingcontextsample.services.CharacterService
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
    suspend fun retrieveCharacterById(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("Request received, search character id: $id")

        return ResponseEntity.ok(characterService.retrieveCharacter(id))
    }

//    @GetMapping("{id}")
//     fun retrieveCharacterById(@PathVariable id: String): Mono<Any> {
//        logger.info("Request received, search character id: $id")
//
//        return characterService.retrieveCharacter(id)
//    }
}