package com.habitasphere.controller;

import com.habitasphere.dto.SocietyRequest;
import com.habitasphere.dto.SocietyResponse;
import com.habitasphere.service.SocietyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/societies")
@RequiredArgsConstructor
public class SocietyController {

    private final SocietyService societyService;

    @PostMapping
    public ResponseEntity<SocietyResponse> createSociety(
            @Valid @RequestBody SocietyRequest request) {

        return new ResponseEntity<>(
                societyService.createSociety(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<SocietyResponse>> getAllSocieties() {

        return ResponseEntity.ok(societyService.getAllSocieties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyResponse> getSocietyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                societyService.getSocietyById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocietyResponse> updateSociety(
            @PathVariable Long id,
            @Valid @RequestBody SocietyRequest request) {

        return ResponseEntity.ok(
                societyService.updateSociety(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSociety(
            @PathVariable Long id) {

        societyService.deleteSociety(id);

        return ResponseEntity.ok("Society deleted successfully");
    }
}