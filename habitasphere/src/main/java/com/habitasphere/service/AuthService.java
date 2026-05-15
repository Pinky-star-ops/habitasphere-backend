package com.habitasphere.service;

import com.habitasphere.dto.LoginRequest;
import com.habitasphere.dto.RegisterRequest;
import com.habitasphere.entity.Role;
import com.habitasphere.entity.RoleType;
import com.habitasphere.entity.User;
import com.habitasphere.repository.RoleRepository;
import com.habitasphere.repository.SocietyRepository;
import com.habitasphere.repository.UserRepository;
import com.habitasphere.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.habitasphere.exception.BadRequestException;
import com.habitasphere.exception.ResourceNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException(
        "Email already exists"
);
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setName(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setActive(
                request.getActive() != null ? request.getActive() : true
        );

        if (request.getSocietyId() == null) {
            throw new RuntimeException("societyId is required");
        }

        user.setSociety(societyRepository.findById(request.getSocietyId())
                .orElseThrow(() ->
                        new RuntimeException("Society not found")));

        // DEFAULT ROLE = USER
        Role userRole = roleRepository
                .findByName(RoleType.ROLE_USER)
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        Set<Role> roles = new HashSet<>();

        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // LOGIN
    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
        "User not found"
));

        if (passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            return jwtUtil.generateToken(user.getEmail());
        }

        return "Invalid Password";
    }
}
