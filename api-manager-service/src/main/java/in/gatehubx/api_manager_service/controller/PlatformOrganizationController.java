package in.gatehubx.api_manager_service.controller;

import in.gatehubx.api_manager_service.dto.requestDto.OrganizationRequest;
import in.gatehubx.api_manager_service.dto.requestDto.OrganizationStatusRequest;
import in.gatehubx.api_manager_service.dto.responseDto.OrganizationResponse;
import in.gatehubx.api_manager_service.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Platform Admin can Perform CRUD operations
 */
@RestController
@RequestMapping("/api/v1/platform/organizations")
@RequiredArgsConstructor
public class PlatformOrganizationController {

    private final OrganizationService organizationService;


    /**
     * Creates an organization End-Point.
     *
     * <p>The organization creation process to the service layer.
     *  The organization is created with Pending status
     * </p>
     *
     * @param request organization creation request containing name and slug
     * @return created organization details
     */
    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationService.create(request));
    }

    /**
     * Read all Organization
     * <p>
     *     Calls the service to List all Organizations present in the system.
     * </p>
     * @return OrganizationResponse List
     * */
    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAll(){
        return ResponseEntity.ok(organizationService.findAll());
    }

    /**
     * Read Organization
     * <p>
     *     Calls the service to fetch single Organizations by their ids
     * </p>
     * @param id of the desired organization
     * @return OrganizationResponse
     * */
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(organizationService.findById(id));
    }

    /**
     * Update Organization
     * <p>
     *     Calls the service to update the organization
     * </p>
     * @param id of the desired organization to modify
     * @param request contains modification data
     * @return OrganizationResponse
     * */
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.ok(organizationService.update(id,request));
    }

    /**
     * Update the Organization's Status
     * <p>
     *     Calls the service to update the organization status
     * </p>
     * @param id of the desired organization for changing the status
     * @param request the desired status
     * @return OrganizationResponse with modified ID
     * */
    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateStatus(@PathVariable Long id,
                                                             @Valid @RequestBody
                                                             OrganizationStatusRequest request){
        return ResponseEntity.ok(organizationService.updateStatus(id, request));
    }

    /**
     * Delete (Soft Delete)
     * <p>
     *     Calls the service to delete (Soft) organization
     * </p>
     * @param id of the organization
     * @return void
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
