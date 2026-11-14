package in.gatehubx.api_manager_service.service;

import in.gatehubx.api_manager_service.dto.requestDto.LoginRequest;
import in.gatehubx.api_manager_service.dto.responseDto.LoginResponse;
import in.gatehubx.api_manager_service.entity.ManagementUsers;
import in.gatehubx.api_manager_service.enums.OrganizationStatus;
import in.gatehubx.api_manager_service.exceptions.BusinessLogicException;
import in.gatehubx.api_manager_service.exceptions.InvalidCredentialsException;
import in.gatehubx.api_manager_service.repository.ManagementUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    //autowiring
    private final ManagementUserRepository  managementUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    //Variables
    @Value("${security.jwt.expiration-seconds}")
    private long expirationSeconds;
    private final String INVALID_CREDENTIAL_EXCEPTION_MSG = "Invalid username or password";

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        ManagementUsers user = managementUserRepository.findByUserName(request.username())
                .orElseThrow(()-> new InvalidCredentialsException(HttpStatus.UNAUTHORIZED,
                        INVALID_CREDENTIAL_EXCEPTION_MSG));

        if(!user.isEnabled() || !passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException(HttpStatus.UNAUTHORIZED,
                    INVALID_CREDENTIAL_EXCEPTION_MSG);
        }

        if(user.getOrganizations() != null && user.getOrganizations().getStatus() != OrganizationStatus.ACTIVE){
            throw new BusinessLogicException(HttpStatus.FORBIDDEN,
                    "Organization is not active");
        }

        Instant issuedAt = Instant.now();
        String role = user.getRoles().getName();
        Long organizationId = user.getOrganizations() == null
                ?null
                :user.getOrganizations().getId();

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("gatehubx-api-manager")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expirationSeconds))
                .subject(user.getUserName())
                .claim("role", role);

        if(organizationId != null){
            claims.claim("organizationId", organizationId);
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(header,claims.build()))
                .getTokenValue();

        return new LoginResponse(token,
                "Bearer",
                expirationSeconds,
                role,
                organizationId);
    }
}
