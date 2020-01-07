package com.poc.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
//@SpringBootApplication
@EnableNeo4jAuditing
@EnableEurekaClient
@EnableNeo4jRepositories(basePackages = "com.poc.ig.certification.repository")
public class CertificationApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
	return new RestTemplate();
	}
	
	@Bean
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.findAndRegisterModules();
		jsonMapper.setSerializationInclusion(Include.NON_NULL);
		jsonMapper.registerModule(new Jdk8Module());
		jsonMapper.registerModule(new JavaTimeModule());
		jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return jsonMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(CertificationApplication.class, args);
	}

}
