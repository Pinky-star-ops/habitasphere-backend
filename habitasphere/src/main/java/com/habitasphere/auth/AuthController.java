package com.habitasphere.auth;

import com.habitasphere.dto.LoginRequest;
import com.habitasphere.dto.RegisterRequest;
import com.habitasphere.service.AuthService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController("authApiController")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return Map.of("token", token);
    }

    @GetMapping("/test")
    public String test() {

        return "Auth working";
    }
}