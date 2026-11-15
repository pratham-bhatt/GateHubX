package in.gatehubx.api_manager_service.repository;

import in.gatehubx.api_manager_service.entity.Organizations;
import in.gatehubx.api_manager_service.enums.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organizations, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);

    List<Organizations> findAllByStatusNot(OrganizationStatus organizationStatus);

    Optional<Organizations> findByIdAndStatusNot(Long id, OrganizationStatus status);
}
