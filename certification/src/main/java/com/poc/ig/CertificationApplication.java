package com.poc.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableNeo4jAuditing
@EnableEurekaClient
@EnableNeo4jRepositories(basePackages = "com.poc.ig.certification.repository")
public class CertificationApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
	return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(CertificationApplication.class, args);
	}

}
