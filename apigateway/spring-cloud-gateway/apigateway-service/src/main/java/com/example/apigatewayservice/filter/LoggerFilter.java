package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggerFilter extends AbstractGatewayFilterFactory<LoggerFilter.Config> {
    public LoggerFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        // exchnage : 웹 객체 가져오기 위한 변수, chain : filter 설정
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            log.info("Global Filter baseMessage : {}", config.getBaseMessage());
//
//            if(config.isPreLogger())
//                log.info("Global Filter start: request id -> {}", request.getId());
//
//            // chain.filter 응답 filter 추가, Mono 비동기 방식에서 단일값 지원할때 사용
//            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
//                if(config.isPostLogger())
//                    log.info("Global Filter end: response code -> {}", response.getStatusCode());
//            }));
//        };
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Logging Filter baseMessage : {}", config.getBaseMessage());
            if(config.isPreLogger())
                log.info("Logging PRE Filter: request id -> {}", request.getId());
            // chain.filter 응답 filter 추가, Mono 비동기 방식에서 단일값 지원할때 사용
            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                if(config.isPostLogger())
                    log.info("Logging POST Filter: response code -> {}", response.getStatusCode());
            }));
        }, Ordered.HIGHEST_PRECEDENCE); // 우선순위 가장 높게
        return filter;
    }

    @Data
    public static class Config{
        // Put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
