package com.poc.ig.imp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class ImportApplication {
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
	return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ImportApplication.class, args);
	}
}
