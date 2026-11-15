package in.gatehubx.api_manager_service.dto.responseDto;

import in.gatehubx.api_manager_service.entity.Organizations;
import in.gatehubx.api_manager_service.enums.OrganizationStatus;

import java.time.LocalDateTime;

public record OrganizationResponse(
        Long id,
        String name,
        String slug,
        OrganizationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * Converts Organizations Entity to the OrganizationResponse Dto
     * @param organizations entity
     * @return OrganizationResponse
     * */
    public static OrganizationResponse from(Organizations organizations){
        return new OrganizationResponse(
                organizations.getId(),
                organizations.getName(),
                organizations.getSlug(),
                organizations.getStatus(),
                organizations.getCreatedAt(),
                organizations.getUpdatedAt()
        );
    }
}
