package com.poc.demo;

import com.poc.demo.util.SSLUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		SSLUtils.disableSslVerification();

		System.out.println("started");
	}

}
