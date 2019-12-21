package com.poc.ig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jAuditing
@EnableNeo4jRepositories(basePackages = "com.poc.ig.repo.repository")
public class RepositoryGraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepositoryGraphApplication.class, args);
	}

}
