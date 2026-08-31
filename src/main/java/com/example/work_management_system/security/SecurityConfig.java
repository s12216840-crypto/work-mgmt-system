package com.example.work_management_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authenticationProvider(
                        authenticationProvider
                )

                .authorizeHttpRequests(auth -> auth

                        // Authentication
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login"
                        ).permitAll()

                        // Error endpoint
                        .requestMatchers("/error")
                        .permitAll()

                        // Users
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/users/**"
                        ).authenticated()

                        .requestMatchers(
                                "/users/**"
                        ).hasRole("ADMIN")

                        // Organizations
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/organizations/**"
                        ).authenticated()

                        .requestMatchers(
                                "/organizations/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Teams
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/teams/**"
                        ).authenticated()

                        .requestMatchers(
                                "/teams/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Projects
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/projects/**"
                        ).authenticated()

                        .requestMatchers(
                                "/projects/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Task search and viewing
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/tasks/**"
                        ).authenticated()

                        // Task creation/update
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                org.springframework.http.HttpMethod.PUT,
                                "/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        // Task assignment
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/tasks/*/assignee/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Task status / priority
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/tasks/*/status/*",
                                "/tasks/*/priority/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        // Delete tasks
                        .requestMatchers(
                                org.springframework.http.HttpMethod.DELETE,
                                "/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Everything else
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}