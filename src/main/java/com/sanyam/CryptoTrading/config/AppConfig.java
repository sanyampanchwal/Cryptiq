package com.sanyam.CryptoTrading.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration // Marks this class as a Spring configuration class
public class AppConfig {
    // Defines how HTTP security should behave (JWT-based, stateless)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // No session tracking, perfect for REST APIs
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Protect /api/** routes, allow all others
                .authorizeHttpRequests(a -> a.requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())

                // Validate JWT token before basic auth
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                // Disable CSRF (not needed for stateless REST)
                .csrf(csrf -> csrf.disable())

                // Enable CORS for frontend-backend communication
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build(); // Build final security configuration
    }

    // Will hold allowed CORS settings (frontend URLs etc.)
    private CorsConfigurationSource corsConfigurationSource() {
        return null;
    }
}
