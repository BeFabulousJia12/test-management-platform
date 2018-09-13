package com.test.collectionService.TestPlatformServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TestPlatformServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestPlatformServerApplication.class, args);
	}
}
