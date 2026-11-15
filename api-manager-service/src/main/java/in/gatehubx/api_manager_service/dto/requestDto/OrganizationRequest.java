package in.gatehubx.api_manager_service.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrganizationRequest(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
        message = "slug must contains lowercase letters, numbers and single hyphens")
        String slug
) {
}
