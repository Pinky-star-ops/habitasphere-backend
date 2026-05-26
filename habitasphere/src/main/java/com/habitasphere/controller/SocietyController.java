package com.habitasphere.controller;

import com.habitasphere.dto.SocietyRequest;
import com.habitasphere.dto.SocietyResponse;
import com.habitasphere.service.SocietyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/societies")
@RequiredArgsConstructor
public class SocietyController {

    private final SocietyService societyService;

    // Admin and Secretary can add new societies.
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    @PostMapping
    public ResponseEntity<SocietyResponse> createSociety(@Valid @RequestBody SocietyRequest request) {
        SocietyResponse response = societyService.createSociety(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Any logged-in user can view society list.
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<SocietyResponse>> getAllSocieties() {
        return ResponseEntity.ok(societyService.getAllSocieties());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<SocietyResponse> getSocietyById(@PathVariable Long id) {
        return ResponseEntity.ok(societyService.getSocietyById(id));
    }

    // Admin and Secretary can update society details.
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    @PutMapping("/{id}")
    public ResponseEntity<SocietyResponse> updateSociety(@PathVariable Long id,
                                                         @Valid @RequestBody SocietyRequest request) {
        return ResponseEntity.ok(societyService.updateSociety(id, request));
    }

    // Only Admin can delete a society.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSociety(@PathVariable Long id) {
        societyService.deleteSociety(id);
        return ResponseEntity.ok("Society deleted successfully");
    }
}
