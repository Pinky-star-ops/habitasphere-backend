package com.habitasphere.controller;

import com.habitasphere.dto.ApartmentRequest;
import com.habitasphere.entity.Apartment;
import com.habitasphere.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    // Admin and Secretary can create apartments.
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    @PostMapping
    public Apartment createApartment(@Valid @RequestBody ApartmentRequest request) {
        return apartmentService.createApartment(request);
    }

    // Admin, Secretary and Residents can view apartments.
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY','RESIDENT')")
    @GetMapping
    public List<Apartment> getAllApartments() {
        return apartmentService.getAllApartments();
    }

    // Only Admin can delete apartments.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.ok("Apartment deleted successfully");
    }
}
