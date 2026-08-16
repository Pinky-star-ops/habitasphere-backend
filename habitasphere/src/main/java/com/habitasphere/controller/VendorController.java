package com.habitasphere.controller;

import com.habitasphere.dto.VendorDTO;
import com.habitasphere.enums.VendorServiceType;
import com.habitasphere.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // Admin and Secretary can add vendors
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(
            @RequestBody VendorDTO dto) {

        return ResponseEntity.ok(
                vendorService.createVendor(dto)
        );
    }

    // Residents can view vendors
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {

        return ResponseEntity.ok(
                vendorService.getAllVendors()
        );
    }

    // Residents can view individual vendors
    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vendorService.getVendorById(id)
        );
    }

    // Residents can filter vendors by service type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<VendorDTO>> getVendorsByType(
            @PathVariable VendorServiceType type) {

        return ResponseEntity.ok(
                vendorService.getVendorsByType(type)
        );
    }

    // Admin and Secretary can update vendors
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> updateVendor(
            @PathVariable Long id,
            @RequestBody VendorDTO dto) {

        return ResponseEntity.ok(
                vendorService.updateVendor(id, dto)
        );
    }

    // Admin and Secretary can deactivate vendors
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateVendor(
            @PathVariable Long id) {

        vendorService.deactivateVendor(id);

        return ResponseEntity.ok(
                "Vendor deactivated successfully"
        );
    }
}