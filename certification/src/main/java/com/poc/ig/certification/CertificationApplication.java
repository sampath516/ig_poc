package com.poc.ig.certification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableNeo4jAuditing
@EnableNeo4jRepositories(basePackages = "com.poc.ig.certification.repository")
public class CertificationApplication {
	
	@Bean
	public RestTemplate getRestTemplate(){
	return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(CertificationApplication.class, args);
	}

}
