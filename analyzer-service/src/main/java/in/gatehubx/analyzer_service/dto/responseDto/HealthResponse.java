package in.gatehubx.analyzer_service.dto.responseDto;

public record HealthResponse(
        String serviceName,
        String serviceDescription,
        String appStatus
) {
}
