package net.thanhdevjava.to_do_list.config;

import lombok.RequiredArgsConstructor;
import net.thanhdevjava.to_do_list.security.custom.CustomAuthenticationEntryPoint;
import net.thanhdevjava.to_do_list.security.custom.CustomUserDetailsService;
import net.thanhdevjava.to_do_list.security.jwt.JwtAuthenticationFilter;
import net.thanhdevjava.to_do_list.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration // Marks this class as a configuration class for Spring
@RequiredArgsConstructor  // Lombok will auto-generate a constructor for all final fields
public class SecurityConfig {

    // Injecting CustomUserDetailsService and JwtUtils
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * CORS configuration to allow requests from the frontend.
     * You should replace "http://localhost:3000" with the actual domain of your frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    /**
     * Main security configuration.
     * - Permit access to authentication endpoints (e.g., login/register)
     * - Require authentication for all other requests
     * - Use JWT (stateless)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll() // Public routes (login, register, etc.)
                                .anyRequest().authenticated() // All other routes require authentication
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Don't use sessions; use JWT instead
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (not needed with JWT for APIs)
                .cors(Customizer.withDefaults()) // Enable CORS with default settings
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter before default auth filter

        return http.build();
    }

    /**
     * Custom JWT filter bean.
     * This filter will intercept requests to extract and validate the JWT token.
     */
    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }

    /**
     * Bean to configure authentication manager.
     * This manager will use our custom UserDetailsService and BCrypt password encoder.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    /**
     * Bean to encode passwords using BCrypt (recommended for security).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
