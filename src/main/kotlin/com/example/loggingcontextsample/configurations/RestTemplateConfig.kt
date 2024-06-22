package com.example.loggingcontextsample.configurations

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.http.io.SocketConfig
import org.apache.hc.core5.util.Timeout
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplatePooledClient(@Value("\${client.base-url}") baseUrl: String): RestTemplate {
        val restTemplateBuilder = RestTemplateBuilder()
        val restTemplate: RestTemplate = restTemplateBuilder.rootUri(baseUrl).build()

        val connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(5))
            .build()

        val socketConfig = SocketConfig.custom()
            .setSoTimeout(Timeout.ofSeconds(5))
            .build()

        val requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofSeconds(5))
            .build()

        val connectionManager = PoolingHttpClientConnectionManager()
        connectionManager.defaultSocketConfig = socketConfig
        connectionManager.setDefaultConnectionConfig(connectionConfig)
        connectionManager.maxTotal = 1000
        connectionManager.defaultMaxPerRoute = 1000

        val httpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build()
        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

        return restTemplate
    }
}

