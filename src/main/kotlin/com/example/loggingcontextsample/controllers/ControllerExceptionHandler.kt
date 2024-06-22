package com.example.loggingcontextsample.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)

    // this generic error handling is only for tests, for production never return details of the exception
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Any> {
        logger.info("Handling exception in thread: ${Thread.currentThread().name}")
        logger.error(exception.message, exception.stackTrace.toString())
        return ResponseEntity
            .internalServerError()
            .body(object {
                val message = exception.message
                val stackTrace = exception.stackTrace
            })
    }
}