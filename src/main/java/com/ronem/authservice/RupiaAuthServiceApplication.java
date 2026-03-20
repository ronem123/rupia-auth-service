package com.ronem.authservice;

import com.ronem.rupiasecuritylib.config.SecurityLibConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(SecurityLibConfig.class)
public class RupiaAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RupiaAuthServiceApplication.class, args);
    }

}
