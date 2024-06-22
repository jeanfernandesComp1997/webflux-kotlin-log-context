package com.example.loggingcontextsample.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration


@Configuration
class WebClientConfig(
    private val webClientBuilder: WebClient.Builder
) {

    @Bean
    fun client(
        @Value("\${client.base-url}")
        baseUrl: String,
    ): WebClient {
        val provider = ConnectionProvider.builder("custom")
            .maxConnections(1000)
            .build()

        val client = HttpClient
            .create(provider)
            .responseTimeout(
                Duration.ofSeconds(5)
            )

        return webClientBuilder
            .clientConnector(ReactorClientHttpConnector(client))
            .baseUrl(baseUrl)
            .build()
    }
}