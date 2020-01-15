package com.wq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Qin.Wei
 */
@SpringBootApplication
@MapperScan("com.wq.mapper")
@EnableSwagger2
@EnableDiscoveryClient
public class WqmallcartUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(WqmallcartUserApplication.class, args);
    }

}
