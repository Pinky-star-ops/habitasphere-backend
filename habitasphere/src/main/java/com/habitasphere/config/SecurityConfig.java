package com.habitasphere.config;

import com.habitasphere.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .requestMatchers("/error")
                        .permitAll()
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/test",
                                "/auth/register",
                                "/auth/login",
                                "/register",
                                "/login",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html"
                        )
                        .permitAll()
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/visitors/**")
                        .hasAnyRole("ADMIN", "SECURITY", "RESIDENT")
                        .requestMatchers("/api/user/**", "/api/users/**")
                        .authenticated()
                        .requestMatchers("/api/societies/**", "/api/apartments/**")
                        .authenticated()
                        .requestMatchers(
        HttpMethod.POST,
        "/api/complaints"
)

.hasAuthority("ROLE_RESIDENT")

.requestMatchers(
        HttpMethod.GET,
        "/api/complaints/my"
)
.hasAuthority("ROLE_RESIDENT")

.requestMatchers(
        HttpMethod.GET,
        "/api/complaints"
)
.hasAuthority("ROLE_ADMIN")

.requestMatchers(
        HttpMethod.PUT,
        "/api/complaints/*/status"
)
.hasAnyAuthority(
        "ROLE_ADMIN",
        "ROLE_SECRETARY"
)
.requestMatchers(
        HttpMethod.POST,
        "/api/notices"
)
.hasAnyAuthority(
        "ROLE_ADMIN",
        "ROLE_SECRETARY"
)

.requestMatchers(
        HttpMethod.PUT,
        "/api/notices/**"
)
.hasAnyAuthority(
        "ROLE_ADMIN",
        "ROLE_SECRETARY"
)

.requestMatchers(
        HttpMethod.DELETE,
        "/api/notices/**"
)
.hasAnyAuthority(
        "ROLE_ADMIN",
        "ROLE_SECRETARY"
)

.requestMatchers(
        HttpMethod.GET,
        "/api/notices/**"
)
.authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
        
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            var authentication = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            log.debug("Access denied for '{}'. Authenticated authorities: {}",
                    request.getRequestURI(),
                    authentication == null ? "none" : authentication.getAuthorities());

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"status\":403,\"message\":\"Access denied. You do not have permission to access this API.\"}");
        };
    }
}
