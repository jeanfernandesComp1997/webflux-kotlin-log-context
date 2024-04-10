package com.example.loggingcontextsample.configurations

import io.micrometer.context.ContextRegistry
import io.micrometer.core.instrument.kotlin.asContextElement
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.contextpropagation.ObservationAwareSpanThreadLocalAccessor
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import reactor.netty.Metrics

@Configuration
class LogTracerConfig(
    private val observationRegistry: ObservationRegistry,
    tracer: Tracer
) {

    init {
        ContextRegistry.getInstance().registerThreadLocalAccessor(ObservationAwareSpanThreadLocalAccessor(tracer));
        ObservationThreadLocalAccessor.getInstance().observationRegistry = observationRegistry
        Metrics.observationRegistry(observationRegistry)
    }

    @Bean
    fun coroutineWebFilter(): WebFilter {
        return object : CoWebFilter() {
            override suspend fun filter(exchange: ServerWebExchange, chain: CoWebFilterChain) =
                withContext(observationRegistry.asContextElement()) {
                    chain.filter(exchange)
                }
        }
    }
}