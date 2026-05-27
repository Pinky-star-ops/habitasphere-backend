package com.habitasphere.security;

import com.habitasphere.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> authorities = user.getRoles() == null
                ? List.of()
                : user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(normalizeRole(role.getName().name())))
                .toList();

        log.debug("Loaded user '{}' with authorities {}", user.getEmail(), authorities);

        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    private String normalizeRole(String roleName) {
        return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
    }
}
