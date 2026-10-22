package in.gatehubx.api_manager_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize -> authorize
                .requestMatchers("/api/v1/api-manager/health").permitAll()
                .anyRequest().authenticated()
        ));

        return http.build();
    }
}