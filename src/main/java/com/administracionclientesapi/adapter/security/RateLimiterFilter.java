package com.administracionclientesapi.adapter.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class RateLimiterFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);
    private final Bucket bucket;
    private String authToken;

    public RateLimiterFilter(@Value("${rate.limiter.capacity}") int capacity,
                             @Value("${rate.limiter.tokens}") int tokens,
                             @Value("${rate.limiter.duration}") Duration duration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(tokens, duration));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
        this.authToken = authToken;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("Request URI: {}", exchange.getRequest().getURI());
        logger.info("Client IP: {}", exchange.getRequest().getRemoteAddress());

        ServerHttpRequest request = exchange.getRequest();

        String userAgent = request.getHeaders().getFirst("User-Agent");
        logger.info("User-Agent: {}", userAgent);

        String method = exchange.getRequest().getMethodValue();
        logger.debug("HTTP Method: {}", method);

        if (method.equalsIgnoreCase("OPTIONS")) {
            logger.error("Skipping OPTIONS request");
            return chain.filter(exchange);
       }

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            logger.error("Se esta bleoqueando la API administracion de clientes por exceso de solicitudes");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return response.setComplete();
        }
    }
}


