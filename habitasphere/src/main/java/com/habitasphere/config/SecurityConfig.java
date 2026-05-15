package com.habitasphere.config;

import com.habitasphere.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

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
                        .requestMatchers(HttpMethod.POST,
                                "/auth/register",
                                "/auth/login",
                                "/register",
                                "/login",
                                "/api/auth/register",
                                "/api/auth/login")
                        .permitAll()
                        .requestMatchers("/api/user/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                           .requestMatchers(HttpMethod.POST, "/api/societies/**")
.hasRole("ADMIN")

.requestMatchers(HttpMethod.PUT, "/api/societies/**")
.hasRole("ADMIN")

.requestMatchers(HttpMethod.DELETE, "/api/societies/**")
.hasRole("ADMIN")
.requestMatchers(HttpMethod.GET, "/api/societies/**")
.authenticated()
                        .anyRequest()
                        .authenticated()
                             )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
