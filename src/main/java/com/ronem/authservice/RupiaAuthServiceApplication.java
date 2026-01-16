package com.ronem.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RupiaAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RupiaAuthServiceApplication.class, args);
    }

}
