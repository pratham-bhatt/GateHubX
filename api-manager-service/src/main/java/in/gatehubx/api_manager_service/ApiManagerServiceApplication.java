package in.gatehubx.api_manager_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class ApiManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiManagerServiceApplication.class, args);
	}

}
