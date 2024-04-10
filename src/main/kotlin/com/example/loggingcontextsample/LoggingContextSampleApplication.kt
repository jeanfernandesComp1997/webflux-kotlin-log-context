package com.example.loggingcontextsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoggingContextSampleApplication

fun main(args: Array<String>) {
    runApplication<LoggingContextSampleApplication>(*args)
}
