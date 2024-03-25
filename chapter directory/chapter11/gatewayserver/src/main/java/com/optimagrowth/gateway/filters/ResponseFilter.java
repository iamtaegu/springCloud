package com.optimagrowth.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracer;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

    final Logger logger =LoggerFactory.getLogger(ResponseFilter.class);

    // 추적ID 및 스팬ID 정보에 접근하는 진입점
    @Autowired
    Tracer tracer;

    @Autowired
    FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                String traceId = tracer.currentSpan().context().traceIdString();
                logger.debug("Adding the correlation id to the outbound headers. {}", traceId);
                // 슬루스 추적ID의 응답 헤더 tmx-correlation-id에 스팬 추가
                exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
                logger.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
            }));
        };
    }
}

