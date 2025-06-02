package com.lawlink.lawlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lawlink")
public class LawlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(LawlinkApplication.class, args);
	}

}
