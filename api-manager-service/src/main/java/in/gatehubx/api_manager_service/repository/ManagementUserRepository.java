package in.gatehubx.api_manager_service.repository;

import in.gatehubx.api_manager_service.entity.ManagementUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagementUserRepository extends JpaRepository<ManagementUsers, Long> {

    Optional<ManagementUsers> findByUserName(String username);
}
