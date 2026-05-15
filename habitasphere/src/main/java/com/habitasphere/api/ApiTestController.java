package com.habitasphere.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiTestController {

    @GetMapping("/user/test")
    public Map<String, Object> userTest(Authentication authentication) {
        return Map.of(
                "message", "User endpoint reachable",
                "principal", authentication == null ? null : authentication.getName()
        );
    }

    @GetMapping("/admin/test")
    public Map<String, Object> adminTest(Authentication authentication) {
        return Map.of(
                "message", "Admin endpoint reachable",
                "principal", authentication == null ? null : authentication.getName()
        );
    }
}

