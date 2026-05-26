package com.habitasphere.controller;

import com.habitasphere.dto.UserProfileResponse;
import com.habitasphere.entity.User;
import com.habitasphere.exception.ResourceNotFoundException;
import com.habitasphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/user", "/api/users"})
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Any logged-in user can access profile APIs.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String userProfile() {
        return "User Profile Accessed";
    }

    // GET /api/users/me - current logged-in user profile.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Extract role strings
        List<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());

        String apartmentNumber = user.getApartment() != null ? user.getApartment().getApartmentNumber() : null;
        String societyName = user.getSociety() != null ? user.getSociety().getName() : null;

        // DTO avoids exposing password and circular entity mappings.
        UserProfileResponse response = UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .apartmentNumber(apartmentNumber)
                .societyName(societyName)
                .build();

        return ResponseEntity.ok(response);
    }
}
