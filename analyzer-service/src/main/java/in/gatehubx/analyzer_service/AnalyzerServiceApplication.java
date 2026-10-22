package in.gatehubx.analyzer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class AnalyzerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerServiceApplication.class, args);
	}

}
