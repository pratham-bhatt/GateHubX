package in.gatehubx.api_manager_service.dto.requestDto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
