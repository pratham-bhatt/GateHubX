package in.gatehubx.api_manager_service.entity;

import in.gatehubx.api_manager_service.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
public class Organizations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", length = 150)
    private String name;

    @Column(name = "SLUG", length = 100)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private OrganizationStatus status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;
}
