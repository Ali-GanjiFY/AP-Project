package org.example.backend.config;

import org.example.backend.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // manual constructor, no lombok in this project's pom.xml
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // BCrypt for hashing/verifying passwords, used directly in AuthService
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // NOTE: no AuthenticationManager/DaoAuthenticationProvider here —
    // AuthService checks passwords manually via passwordEncoder.matches(),
    // it doesn't go through Spring's AuthenticationManager.
    // Add those back only if you build a CustomUserDetailsService later.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // restrict this in production
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // stateless API, no cookies -> no CSRF needed
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // always-public endpoints, regardless of HTTP method
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/docs/**"
                        ).permitAll()

                        // read-only browsing is public, but ONLY for GET.
                        // Any future POST/PUT/DELETE on these paths (e.g. creating/
                        // editing an advertisement, or admin CRUD on categories/cities)
                        // is NOT covered here and falls through to anyRequest().authenticated()
                        // below, or should be pinned to hasRole("ADMIN") explicitly if needed.
                        .requestMatchers(HttpMethod.GET,
                                "/api/advertisements/**",
                                "/api/cities/**",
                                "/api/categories/**"
                        ).permitAll()

                        // admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // example: writes on cities/categories are admin-managed data,
                        // lock them down explicitly instead of relying on the fallback
                        .requestMatchers(HttpMethod.POST, "/api/cities/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cities/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cities/**", "/api/categories/**").hasRole("ADMIN")

                        // everything else (advertisement create/update/delete, ratings,
                        // messages, favorites, etc.) requires a valid JWT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}