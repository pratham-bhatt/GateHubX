package in.gatehubx.api_manager_service.service;

import in.gatehubx.api_manager_service.dto.responseDto.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HealthService {

    @Value("${info.app.name}")
    private String serviceName;

    @Value("${info.app.description}")
    private String serviceDescription;

    private final JdbcTemplate jdbcTemplate;

    public HealthResponse getHealthInfo(){

        String dbStatus = "DOWN";

        try{
           Integer result = jdbcTemplate
                   .queryForObject("SELECT 1",
                           Integer.class);
           if(Integer.valueOf(1).equals(result)){
               dbStatus = "UP";
           }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return  new HealthResponse(serviceName,
                serviceDescription,
                "UP",
                dbStatus,
                LocalDateTime.now());
    }

}
