package com.gblins.devicetracker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ApiKeyFilter : WebFilter {

    @Value($$"${security.api-key}")
    private lateinit var secretKey: String

    // Lista com as rotas protegidas e seus métodos HTTP.
    private val protectedRoutes = listOf(
        "/api/v1/pings" to HttpMethod.GET
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val requestPath = exchange.request.path.value()
        val requestMethod = exchange.request.method

        val isProtected = protectedRoutes.any { (path, method) ->
            requestPath.startsWith(path) && requestMethod == method
        }

        if (isProtected) {
            val apiKeyHeader = exchange.request.headers.getFirst("X-API-KEY")

            if (apiKeyHeader == null || apiKeyHeader != secretKey) {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return exchange.response.setComplete()
            }
        }

        return chain.filter(exchange)
    }
}