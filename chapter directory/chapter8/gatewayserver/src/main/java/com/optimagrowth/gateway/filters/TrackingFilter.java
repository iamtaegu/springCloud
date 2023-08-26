package com.optimagrowth.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/** Concepts
 * 스프링 클라우드 게이트웨이 필터
 * 	ㅁ 게이트웨이로 유입되는 모든 요청을 검사
 * 	ㅁ 요청에서 tmx-correlation-id라는 HTTP 헤더 포함 여부를 확인하는 TrackingFilter
 *  ㅁ tmx-correlation-id 헤더에는 여러 마이크로서비스를 거쳐 사용자 요청을 추적하는 데 사용되는 고유한 GUID가 포함
 */

/** Code
 *  ㅁ 글로벌 필터는 GlobalFilter 인터페이스를 구현하고 filter() 메서드를 재정의
 *  ㅁ 여러 필터에 걸쳐 공통으로 사용되는 함수는 FilterUtils 클래스에 캡슐화
 *
 */

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

	@Autowired
	FilterUtils filterUtils;

	/**
	 * 요청이 필터를 통과할 때마다 실행되는 코드
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// ServerWebExchange 객체를 사용하여 요청에서 ServerWebExchange 객체 HTTP 헤더 추출
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(requestHeaders)) {
			logger.debug("tmx-correlation-id found in tracking filter: {}. ", 
					filterUtils.getCorrelationId(requestHeaders));
		} else {
			String correlationID = generateCorrelationId();
			exchange = filterUtils.setCorrelationId(exchange, correlationID);
			logger.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID);
		}
		
		return chain.filter(exchange);
	}


	/**
	 * 요청 헤더에 상관관계 ID가 있는지 확인하는 헬퍼 메서드
	 */
	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		if (filterUtils.getCorrelationId(requestHeaders) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 상관관계 ID를 UUID 값으로 생성
	 */
	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}

}
