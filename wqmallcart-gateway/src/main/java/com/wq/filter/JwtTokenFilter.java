package com.wq.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.wq.utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Title: JwtTokenFilter
 * @Description: TODO
 * @Author: 魏秦
 * @Date: 2020/1/8  10:52
 */
@Component
@Data
public class JwtTokenFilter implements GlobalFilter, Ordered {

    private static final String Oauth_Token_Url="user/user";

    private ObjectMapper objectMapper;

    public JwtTokenFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(path.contains(Oauth_Token_Url)){
            return chain.filter(exchange);
        }
        String httpHost="127.0.0.1:9001";
        HttpHeaders headers=exchange.getRequest().getHeaders();
        String host = headers.getHost().toString();
        String cookies = headers.get("Cookie").toString();
        String userPhone=cookies.substring(8,19);
        String tokenValue=cookies.substring(20,cookies.length()-1);
        String userId = JwtUtil.verifyJwt(tokenValue).get("userId").toString();
        if(httpHost.equals(host)&userId!=null){
            //放行
            return chain.filter(exchange);
        }else{
            //拦截
            return exchange.getResponse().setComplete();
        }


    }


    @Override
    public int getOrder() {
        return 0;
    }
}
