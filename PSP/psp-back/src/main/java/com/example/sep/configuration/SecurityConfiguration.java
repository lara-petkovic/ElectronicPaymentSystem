package com.example.sep.configuration;

import com.example.sep.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
             .cors(cors -> cors.configurationSource(request -> {
                 var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                 corsConfig.setAllowedOrigins(List.of("https://localhost:8086", "https://localhost:8087", "https://localhost:4201")); // Dozvoljeni frontend domen
                 corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                 corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                 corsConfig.setExposedHeaders(List.of("Authorization"));
                 corsConfig.setAllowCredentials(true);
                 return corsConfig;
             }))
             .csrf(csrf -> csrf.disable())
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers(
                             "/api/authenticate/register",
                             "/api/authenticate/login",
                             "/api/authenticate/generate-qr/*",
                             "api/subscription",
                             "api/transaction",
                             "api/response",
                             "/v1/api/**",
                             "/v2/api-docs",
                             "/v3/api-docs",
                             "/v3/api-docs/**",
                             "/swagger-resources",
                             "/swagger-resources/**",
                             "/configuration/ui",
                             "/configuration/security",
                             "/swagger-ui/**",
                             "/webjars/**",
                             "/swagger-ui.html",
                             "/metrics",
                             "/actuator/**",
                             "/transactions", "/clients", "/responses"
                     ).permitAll()
                     .anyRequest().authenticated()
             )
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

     return http.build();
 }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
