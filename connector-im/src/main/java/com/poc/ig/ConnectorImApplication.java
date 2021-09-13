package com.poc.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@EnableEurekaClient
public class ConnectorImApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectorImApplication.class, args);
	}

}
