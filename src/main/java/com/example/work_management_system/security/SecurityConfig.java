package com.example.work_management_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                new DaoAuthenticationProvider(userDetailsService);

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

                .authenticationProvider(authenticationProvider)

                .authorizeHttpRequests(auth -> auth

                        // Authentication
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/auth/register",
                                "/auth/login",
                                "/error"
                        ).permitAll()

                        // Users - Milestone 2
                        .requestMatchers(
                                "/api/users/**"
                        ).permitAll()

                        // Organizations - Milestone 3
                        .requestMatchers(
                                "/api/organizations/**"
                        ).permitAll()

                        // Teams - Milestone 3
                        .requestMatchers(
                                "/api/teams/**"
                        ).permitAll()

                        // Projects
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/projects/**"
                        ).authenticated()

                        .requestMatchers(
                                "/api/projects/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Tasks
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/tasks/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/tasks/*/assignee/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/tasks/*/status/*",
                                "/api/tasks/*/priority/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        // Comments
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/tasks/*/comments"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/tasks/*/comments"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/tasks/comments/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/tasks/comments/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        // Labels
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/tasks/*/labels"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/tasks/*/labels/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/tasks/*/labels/*"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER",
                                "DEVELOPER"
                        )

                        // Delete Tasks
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/tasks/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "MANAGER"
                        )

                        // Labels
                        .requestMatchers(
                                "/api/labels/**"
                        ).authenticated()

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