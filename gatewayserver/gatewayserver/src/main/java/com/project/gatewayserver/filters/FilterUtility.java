package com.project.gatewayserver.filters;

import java.util.List;

import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component 
public class FilterUtility {
    
    public static final String CORRELATION_ID = "correlation-id";

    public String getCorrelationId(HttpHeaders reqHeaders){
        if(reqHeaders.get(CORRELATION_ID) != null){
            List<String> reqHeaderList = reqHeaders.get(CORRELATION_ID);
            return reqHeaderList.stream().findFirst().get();
        }else{
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value){
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId){
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }


}
