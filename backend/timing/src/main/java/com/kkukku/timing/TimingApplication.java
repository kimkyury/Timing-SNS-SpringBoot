package com.kkukku.timing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class TimingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimingApplication.class, args);
	}

}
