package com.wq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;

/**
 * @author Qin.Wei
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WqmallcartGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WqmallcartGatewayApplication.class, args);
    }
    @Bean
    public ServerCodecConfigurer serverCodecConfigurer(){
        return ServerCodecConfigurer.create();
    }

}
