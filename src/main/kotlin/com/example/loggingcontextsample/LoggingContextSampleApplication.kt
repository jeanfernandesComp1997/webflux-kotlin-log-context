package com.example.loggingcontextsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class LoggingContextSampleApplication

fun main(args: Array<String>) {
	runApplication<LoggingContextSampleApplication>(*args)
	Hooks.enableAutomaticContextPropagation()
}
