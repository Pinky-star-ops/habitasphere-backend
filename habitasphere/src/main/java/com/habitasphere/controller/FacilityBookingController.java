package com.habitasphere.controller;

import com.habitasphere.dto.FacilityBookingRequest;
import com.habitasphere.dto.FacilityBookingResponse;
import com.habitasphere.service.FacilityBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class FacilityBookingController {

    private final FacilityBookingService facilityBookingService;

    @PostMapping
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<FacilityBookingResponse> bookFacility(
            @Valid @RequestBody FacilityBookingRequest request) {
        FacilityBookingResponse response = facilityBookingService.bookFacility(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<List<FacilityBookingResponse>> getMyBookings() {
        return ResponseEntity.ok(facilityBookingService.getMyBookings());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        facilityBookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<List<FacilityBookingResponse>> getAllBookings() {
        return ResponseEntity.ok(facilityBookingService.getAllBookings());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<FacilityBookingResponse> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(facilityBookingService.approveBooking(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<FacilityBookingResponse> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(facilityBookingService.rejectBooking(id));
    }
}
