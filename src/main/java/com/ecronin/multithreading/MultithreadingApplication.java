package com.ecronin.multithreading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * API DOC: http://localhost:8082/swagger-ui/index.html
 */

@SpringBootApplication
public class MultithreadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultithreadingApplication.class, args);
		System.out.println("Main Thread: " + Thread.currentThread().getName());
	}
}
