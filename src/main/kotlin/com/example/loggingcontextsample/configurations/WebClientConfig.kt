package com.example.loggingcontextsample.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val webClientBuilder: WebClient.Builder
) {

    @Bean
    fun client(
        @Value("\${client.base-url}")
        baseUrl: String,
    ): WebClient {
        return webClientBuilder
            .baseUrl(baseUrl)
            .build()
    }
}