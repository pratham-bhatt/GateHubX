package in.gatehubx.analyzer_service.service;

import in.gatehubx.analyzer_service.dto.responseDto.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {

    @Value("${info.app.name}")
    private String serviceName;

    @Value("${info.app.description}")
    private String serviceDescription;

    public HealthResponse getHealthInfo(){
        return  new HealthResponse(serviceName, serviceDescription, "UP");
    }

}
