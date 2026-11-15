package in.gatehubx.api_manager_service.dto.requestDto;

import in.gatehubx.api_manager_service.enums.OrganizationStatus;
import jakarta.validation.constraints.NotNull;

public record OrganizationStatusRequest(
        @NotNull
        OrganizationStatus status
) {
}
