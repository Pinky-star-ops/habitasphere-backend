package com.habitasphere.controller;

import com.habitasphere.dto.LoginRequest;
import com.habitasphere.dto.RegisterRequest;
import com.habitasphere.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;
    }

    // REGISTER (root + /auth prefix for clients using /auth/register)
    @PostMapping({"/register", "/auth/register"})
    public String register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }

    // LOGIN
    @PostMapping({"/login", "/auth/login"})
    public String login(
            @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }

    // TEST API
    @GetMapping("/test")
    public String test() {

        return "Backend Running Successfully";
    }
}