package com.habitasphere.service;

import com.habitasphere.dto.VendorDTO;
import com.habitasphere.entity.Society;
import com.habitasphere.entity.Vendor;
import com.habitasphere.enums.VendorServiceType;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final SocietyRepository societyRepository;

    public VendorService(
            VendorRepository vendorRepository,
            SocietyRepository societyRepository
    ) {
        this.vendorRepository = vendorRepository;
        this.societyRepository = societyRepository;
    }

    public VendorDTO createVendor(VendorDTO dto) {

        validateVendor(dto);

        Society society = societyRepository.findById(dto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        Vendor vendor = new Vendor();

        vendor.setName(dto.getName());
        vendor.setCompanyName(dto.getCompanyName());
        vendor.setPhone(dto.getPhone());
        vendor.setEmail(dto.getEmail());
        vendor.setServiceType(dto.getServiceType());
        vendor.setAddress(dto.getAddress());
        vendor.setContractStartDate(dto.getContractStartDate());
        vendor.setContractEndDate(dto.getContractEndDate());
        vendor.setIsActive(true);
        vendor.setSociety(society);

        Vendor savedVendor = vendorRepository.save(vendor);

        return convertToDTO(savedVendor);
    }

    public List<VendorDTO> getAllVendors() {

        return vendorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public VendorDTO getVendorById(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        return convertToDTO(vendor);
    }

    public List<VendorDTO> getVendorsByType(VendorServiceType type) {

        return vendorRepository.findByServiceType(type)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public VendorDTO updateVendor(Long id, VendorDTO dto) {

        validateVendor(dto);

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (dto.getSocietyId() != null &&
                !dto.getSocietyId().equals(vendor.getSociety().getId())) {

            Society society = societyRepository.findById(dto.getSocietyId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));

            vendor.setSociety(society);
        }

        vendor.setName(dto.getName());
        vendor.setCompanyName(dto.getCompanyName());
        vendor.setPhone(dto.getPhone());
        vendor.setEmail(dto.getEmail());
        vendor.setServiceType(dto.getServiceType());
        vendor.setAddress(dto.getAddress());
        vendor.setContractStartDate(dto.getContractStartDate());
        vendor.setContractEndDate(dto.getContractEndDate());

        if (dto.getIsActive() != null) {
            vendor.setIsActive(dto.getIsActive());
        }

        Vendor updatedVendor = vendorRepository.save(vendor);

        return convertToDTO(updatedVendor);
    }

    public void deactivateVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setIsActive(false);

        vendorRepository.save(vendor);
    }

    private void validateVendor(VendorDTO dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Vendor name is required");
        }

        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Vendor phone is required");
        }

        if (dto.getServiceType() == null) {
            throw new RuntimeException("Service type is required");
        }

        if (dto.getSocietyId() == null) {
            throw new RuntimeException("Society ID is required");
        }

        if (dto.getContractStartDate() != null &&
                dto.getContractEndDate() != null &&
                dto.getContractEndDate()
                        .isBefore(dto.getContractStartDate())) {

            throw new RuntimeException(
                    "Contract end date cannot be before start date"
            );
        }
    }

    private VendorDTO convertToDTO(Vendor vendor) {

        VendorDTO dto = new VendorDTO();

        dto.setId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setCompanyName(vendor.getCompanyName());
        dto.setPhone(vendor.getPhone());
        dto.setEmail(vendor.getEmail());
        dto.setServiceType(vendor.getServiceType());
        dto.setAddress(vendor.getAddress());
        dto.setContractStartDate(vendor.getContractStartDate());
        dto.setContractEndDate(vendor.getContractEndDate());
        dto.setIsActive(vendor.getIsActive());

        if (vendor.getSociety() != null) {
            dto.setSocietyId(vendor.getSociety().getId());
        }

        return dto;
    }
}