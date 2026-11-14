package in.gatehubx.api_manager_service.dto.errorResponse;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    int status,
    String message,
    Instant timeStamp,
    List<String> description
) {
}
