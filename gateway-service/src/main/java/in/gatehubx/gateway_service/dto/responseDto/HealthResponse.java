package in.gatehubx.gateway_service.dto.responseDto;

public record HealthResponse(
        String serviceName,
        String serviceDescription,
        String appStatus
) {
}
