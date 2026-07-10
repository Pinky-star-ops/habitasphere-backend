package com.habitasphere.service;

import com.habitasphere.dto.FacilityRequest;
import com.habitasphere.dto.FacilityResponse;
import com.habitasphere.entity.Facility;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityResponse createFacility(FacilityRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Facility name cannot be blank");
        }
        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BadRequestException("Capacity must be positive");
        }
        if (facilityRepository.existsByName(request.getName())) {
            throw new BadRequestException("Facility with name '" + request.getName() + "' already exists");
        }

        Facility facility = Facility.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .capacity(request.getCapacity())
                .active(request.isActive())
                .build();

        Facility saved = facilityRepository.save(facility);
        return mapToResponse(saved);
    }

    public FacilityResponse updateFacility(Long id, FacilityRequest request) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Facility name cannot be blank");
        }
        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BadRequestException("Capacity must be positive");
        }

        // Check duplicate name
        facilityRepository.findByName(request.getName().trim()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Facility with name '" + request.getName() + "' already exists");
            }
        });

        facility.setName(request.getName().trim());
        facility.setDescription(request.getDescription());
        facility.setCapacity(request.getCapacity());
        facility.setActive(request.isActive());

        Facility updated = facilityRepository.save(facility);
        return mapToResponse(updated);
    }

    public void deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));
        facilityRepository.delete(facility);
    }

    public List<FacilityResponse> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FacilityResponse getFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));
        return mapToResponse(facility);
    }

    private FacilityResponse mapToResponse(Facility facility) {
        return FacilityResponse.builder()
                .id(facility.getId())
                .name(facility.getName())
                .description(facility.getDescription())
                .capacity(facility.getCapacity())
                .active(facility.isActive())
                .build();
    }
}
