package com.example.loggingcontextsample.configurations

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RequestInterceptor : WebFilter {

    override fun filter(
        serverWebExchange: ServerWebExchange,
        webFilterChain: WebFilterChain
    ): Mono<Void> {

        return webFilterChain.filter(serverWebExchange).contextWrite { context ->
            context.put("userKey", serverWebExchange.request.headers["user-key"] ?: "")
        }
    }
}
