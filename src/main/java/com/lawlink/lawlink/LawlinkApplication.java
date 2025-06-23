package com.lawlink.lawlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.lawlink")
@EnableJpaRepositories(basePackages = "com.lawlink.repository")
@EntityScan(basePackages = "com.lawlink.entity")
public class LawlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(LawlinkApplication.class, args);
    }
}
