package in.gatehubx.api_manager_service.dto.responseDto;

public record LoginResponse(
      String token,
      String tokenType,
      long expiresInSeconds,
      String role,
      Long OrganizationId
) {
}
