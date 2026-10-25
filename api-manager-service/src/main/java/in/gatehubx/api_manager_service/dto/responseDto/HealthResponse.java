package in.gatehubx.api_manager_service.dto.responseDto;

import java.time.LocalDateTime;

public record HealthResponse(
        String serviceName,
        String serviceDescription,
        String serverStatus,
        String dbStatus,
        LocalDateTime timeStamp
) {
}
