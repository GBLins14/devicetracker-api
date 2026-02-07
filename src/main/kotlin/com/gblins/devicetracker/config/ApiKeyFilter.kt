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
    @Value("\${security.api-key-get:}") // API KEY pata rotas GET.
    private var getApiKey: String = ""

    @Value("\${security.api-key-post:}") // API KEY pata rotas POST.
    private var postApiKey: String = ""

    // Lista com as rotas protegidas e seus métodos HTTP.
    private val protectedRoutes = listOf(
        "/api/v1/pings" to HttpMethod.GET,
        "/api/v1/pings" to HttpMethod.POST
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val requestPath = exchange.request.path.value()
        val requestMethod = exchange.request.method

        val isProtected = protectedRoutes.any { (path, method) ->
            requestPath.startsWith(path) && requestMethod == method
        }

        if (isProtected) {
            val expectedKey = when (requestMethod) {
                HttpMethod.GET -> getApiKey
                HttpMethod.POST -> postApiKey
                else -> ""
            }

            val apiKeyHeader = exchange.request.headers.getFirst("X-API-KEY")

            if (expectedKey.isBlank() || apiKeyHeader == null || apiKeyHeader != expectedKey) {
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return exchange.response.setComplete()
            }
        }

        return chain.filter(exchange)
    }
}