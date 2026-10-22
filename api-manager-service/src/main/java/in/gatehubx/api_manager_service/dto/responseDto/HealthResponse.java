package in.gatehubx.api_manager_service.dto.responseDto;

public record HealthResponse(
        String serviceName,
        String serviceDescription,
        String appStatus
) {
}
