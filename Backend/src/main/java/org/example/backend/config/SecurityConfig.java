package org.example.backend.config;

import org.example.backend.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                    // public endpoints
                    // ASSUMPTION: read-only browsing (ads/cities/categories) is public,
                    // confirm against the written project doc if you have a text version.
                    // NOTE: "/api/advertisements/**" covers every sub-path, including
                    // future ones like create/update -> revisit this once that
                    // controller exists, those must require authentication instead.
                    .requestMatchers(
                            "/api/auth/**",
                            "/api/advertisements/**",
                            "/api/cities/**",
                            "/api/categories/**",
                            "/api/health",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/docs/**"
                    ).permitAll()

                    // admin-only endpoints
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")

                    // everything else requires a valid JWT
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
