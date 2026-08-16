package com.habitasphere.controller;

import com.habitasphere.dto.StaffDTO;
import com.habitasphere.enums.StaffType;
import com.habitasphere.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Admin and Secretary can add staff
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @PostMapping
    public ResponseEntity<StaffDTO> createStaff(
            @RequestBody StaffDTO dto) {

        return ResponseEntity.ok(
                staffService.createStaff(dto)
        );
    }

    // Residents can view staff
    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaff() {

        return ResponseEntity.ok(
                staffService.getAllStaff()
        );
    }

    // Residents can view individual staff
    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaffById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                staffService.getStaffById(id)
        );
    }

    // Residents can filter staff by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<StaffDTO>> getStaffByType(
            @PathVariable StaffType type) {

        return ResponseEntity.ok(
                staffService.getStaffByType(type)
        );
    }

    // Admin and Secretary can update staff
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<StaffDTO> updateStaff(
            @PathVariable Long id,
            @RequestBody StaffDTO dto) {

        return ResponseEntity.ok(
                staffService.updateStaff(id, dto)
        );
    }

    // Admin and Secretary can deactivate staff
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateStaff(
            @PathVariable Long id) {

        staffService.deactivateStaff(id);

        return ResponseEntity.ok(
                "Staff deactivated successfully"
        );
    }
}