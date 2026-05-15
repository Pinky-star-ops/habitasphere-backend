package com.habitasphere.security;

import com.habitasphere.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

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

        // IMPORTANT: Spring Security expects authorities to include the `ROLE_` prefix
        // because `hasRole("ADMIN")` maps to `ROLE_ADMIN`.
        List<SimpleGrantedAuthority> authorities = user.getRoles() == null
                ? List.of()
                : user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .toList();

        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}