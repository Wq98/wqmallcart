package com.wq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Qin.Wei
 */
@SpringBootApplication
@MapperScan("com.wq.mapper")
@EnableDiscoveryClient
public class WqmallcartSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WqmallcartSearchApplication.class, args);
    }

}
