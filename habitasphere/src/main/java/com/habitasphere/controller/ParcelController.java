package com.habitasphere.controller;

import com.habitasphere.dto.ParcelRequest;
import com.habitasphere.dto.ParcelResponse;
import com.habitasphere.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    // ADMIN, SECRETARY and SECURITY can register parcels
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY')")
    @PostMapping
    public ResponseEntity<ParcelResponse> createParcel(
            @RequestBody ParcelRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.createParcel(request, username)
        );
    }

    // ADMIN, SECRETARY and SECURITY can view all parcels
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY')")
    @GetMapping
    public ResponseEntity<List<ParcelResponse>> getAllParcels(
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.getAllParcels(username)
        );
    }

    // Admin/staff can view any parcel.
    // Residents will be checked for ownership in the service layer.
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY', 'RESIDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ParcelResponse> getParcelById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.getParcelById(id, username)
        );
    }

    // ADMIN, SECRETARY and SECURITY can update parcels
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY')")
    @PutMapping("/{id}")
    public ResponseEntity<ParcelResponse> updateParcel(
            @PathVariable Long id,
            @RequestBody ParcelRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.updateParcel(id, request, username)
        );
    }

    // ADMIN, SECRETARY and SECURITY can mark parcels as collected
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY')")
    @PutMapping("/{id}/collect")
    public ResponseEntity<ParcelResponse> collectParcel(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.collectParcel(id, username)
        );
    }

    // ADMIN, SECRETARY and SECURITY can mark parcels as returned
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY', 'SECURITY')")
    @PutMapping("/{id}/return")
    public ResponseEntity<ParcelResponse> returnParcel(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.returnParcel(id, username)
        );
    }

    // Residents can view only their own parcels
    @PreAuthorize("hasRole('RESIDENT')")
    @GetMapping("/my")
    public ResponseEntity<List<ParcelResponse>> getMyParcels(
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                parcelService.getMyParcels(username)
        );
    }
}