package in.gatehubx.api_manager_service.service;

import in.gatehubx.api_manager_service.dto.requestDto.OrganizationRequest;
import in.gatehubx.api_manager_service.dto.requestDto.OrganizationStatusRequest;
import in.gatehubx.api_manager_service.dto.responseDto.OrganizationResponse;
import in.gatehubx.api_manager_service.entity.Organizations;
import in.gatehubx.api_manager_service.enums.OrganizationStatus;
import in.gatehubx.api_manager_service.exceptions.BusinessLogicException;
import in.gatehubx.api_manager_service.repository.OrganizationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    /**
     * Creates an organization Service.
     *
     * <p>
     *     The unique organization is created with Pending status.
     *     Throws exception when non-unique name or slug is given
     * </p>
     *
     * @param request organization creation request containing name and slug
     * @return created organization details
     * @throws BusinessLogicException when name or slug exists in the system
     */
    @Transactional
    public OrganizationResponse create(OrganizationRequest request) {

        long startedAt = System.nanoTime();

        checkUnique(request.name(), request.slug(), null);

        Organizations organization = new Organizations();
        organization.setName(request.name());
        organization.setSlug(request.slug());
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());

        organizationRepository.save(organization);

        //TODO: Audit service

        return OrganizationResponse.from(organization);
    }

    /**
     * Read all Organization
     * <p>
     *     List all Organizations present in the system.
     *     DELETED status is not shown
     * </p>
     * @return OrganizationResponse List
     * */
    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll() {

        List<OrganizationResponse> response = new ArrayList<>();

        for (Organizations organization :
                organizationRepository.findAllByStatusNot(OrganizationStatus.DELETED)){
            response.add(OrganizationResponse.from(organization));
        }

        return response;
    }

    /**
     * Read Organization
     * <p>
     *     fetch single Organizations by their ids
     *     DELETED status is not shown
     * </p>
     * @param id of the desired organization
     * @return OrganizationResponse
     * @throws BusinessLogicException if the organization is not found in the system
     * */
    @Transactional(readOnly = true)
    public OrganizationResponse findById(Long id) {
        return OrganizationResponse.from(getOrganization(id));
    }


    /**
     * Update Organization
     * <p>
     *     update the organization
     * </p>
     * @param id of the desired organization to modify
     * @param request contains modification data
     * @return OrganizationResponse
     * @throws BusinessLogicException if duplicate name or slug found
     * @throws BusinessLogicException if organization not found
     * */
    @Transactional
    public OrganizationResponse update(Long id, @Valid OrganizationRequest request) {
        long startedAt = System.nanoTime();

        Organizations organization = getOrganization(id);

        checkUnique(request.name(), request.slug(), id);

        organization.setName(request.name());
        organization.setSlug(request.slug());
        organization.setUpdatedAt(LocalDateTime.now());

        //TODO: Audit service

        return OrganizationResponse.from(organization);
    }

    /**
     * Update the Organization's Status
     * <p>
     *     update the organization status.
     *     <b>DELETE</b> status is not allowed
     * </p>
     * @param id of the desired organization for changing the status
     * @param request the desired status
     * @return OrganizationResponse with modified ID
     * @throws BusinessLogicException if organization not found
     * @throws BusinessLogicException if DELETE status is passed
     * */
    @Transactional
    public OrganizationResponse updateStatus(Long id, @Valid OrganizationStatusRequest request) {

        long startedAt = System.nanoTime();

        Organizations organization = getOrganization(id);

        if(request.status() == OrganizationStatus.DELETED){
            throw new BusinessLogicException(HttpStatus.BAD_REQUEST,
                    "Use DELETE to soft delete and organization");
        }

        organization.setStatus(request.status());
        organization.setUpdatedAt(LocalDateTime.now());

        //TODO: Audit service

        return OrganizationResponse.from(organization);
    }

    /**
     * Delete (Soft Delete)
     * <p>
     *  delete (Soft) organization
     * </p>
     * @param id of the organization
     * */
    @Transactional
    public void delete(Long id) {

        long startedAt = System.nanoTime();

        Organizations organization = getOrganization(id);
        organization.setStatus(OrganizationStatus.DELETED);
        organization.setDeletedAt(LocalDateTime.now());
    }

    //Helper method
    /**
     * Helper Method to check the uniqueness of Organization
     * @param name of the desired organization
     * @param slug the desired status
     * @param currentId of the organization
     * @throws BusinessLogicException if organization name or slug is there in the system
     * */
    private void checkUnique(String name, String slug, Long currentId){

        if(organizationRepository.existsByNameIgnoreCase(name)){
            Organizations current = currentId == null
                    ? null
                    : organizationRepository.findById(currentId).orElse(null);

            if(current == null || !current.getName().equalsIgnoreCase(name)){
                throw new BusinessLogicException(HttpStatus.CONFLICT, "Organization name Already exists");
            }
        }

        if (organizationRepository.existsBySlugIgnoreCase(slug)){
            Organizations current = currentId == null
                    ? null
                    : organizationRepository.findById(currentId).orElse(null);


            if(current == null || !current.getSlug().equalsIgnoreCase(slug)){
                throw new BusinessLogicException(HttpStatus.CONFLICT, "Organization slug Already exists");
            }
        }
    }

    /**
     * Helper Method to check the uniqueness of Organization
     * <p>
     *     Finds the organization  whose status is not deleted 
     * </p>
     * @param id of the desired organization
     * @return Organizations entity of desired id
     * @throws BusinessLogicException if organization not found
     * */
    private Organizations getOrganization(Long id){
        return organizationRepository.findByIdAndStatusNot(id, OrganizationStatus.DELETED)
                .orElseThrow(()-> new BusinessLogicException(HttpStatus.NOT_FOUND, "Organization not found"));
    }
}
