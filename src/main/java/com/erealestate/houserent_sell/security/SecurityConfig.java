package com.erealestate.houserent_sell.security;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API; enable if using CSRF
                                                              // tokens
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                                                                                                         // session for
                                                                                                         // APIs
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/users/signup", // Allow signup
                                                                "/api/users/login", // Allow login
                                                                "/api/users/all", // Allow access to all users
                                                                "/auth/forgot-password", // Forgot password
                                                                "/auth/reset-password", // Reset password
                                                                "/api/users/**", // Allow user-related requests
                                                                "/auth/**",
                                                                "/public/**",
                                                                "/code/**",
                                                                "/property/post", // Allow property posting
                                                                "/api/property/post", // Allow property creation
                                                                "/api/property/postWithFile", // Allow property creation
                                                                                              // with files
                                                                "/api/properties", // Allow property listing
                                                                "/properties/**" // Any other property-related requests
                                                ).permitAll() // Publicly accessible endpoints
                                                .anyRequest().authenticated()) // Secure all other endpoints
                                .build();
        }

        @Bean
        public SessionRegistry sessionRegistry() {
                return new SessionRegistryImpl();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:8090", "http://localhost:3000")); // Add
                                                                                                                  // your
                                                                                                                  // frontend
                                                                                                                  // URLs
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setExposedHeaders(Arrays.asList("Authorization", "X-CSRF-TOKEN")); // Expose CSRF token if
                                                                                                 // required
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L); // Cache CORS configuration for 1 hour

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
