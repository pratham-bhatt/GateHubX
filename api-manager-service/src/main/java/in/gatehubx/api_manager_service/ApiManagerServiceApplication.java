package in.gatehubx.api_manager_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })		// need to remove once DB is configured
public class ApiManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiManagerServiceApplication.class, args);
	}

}
