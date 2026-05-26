package com.habitasphere.controller;

import com.habitasphere.dto.AssignApartmentRequest;
import com.habitasphere.dto.LoginRequest;
import com.habitasphere.dto.RegisterRequest;
import com.habitasphere.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        String response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        String response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    // ASSIGN APARTMENT
    @PostMapping("/assign-apartment")
    public ResponseEntity<?> assignApartment(
            @RequestBody AssignApartmentRequest request
    ) {

        String response = authService.assignApartment(request);

        return ResponseEntity.ok(response);
    }

    // TEST API
    @GetMapping("/test")
    public String test() {

        return "Backend Running Successfully";
    }
}