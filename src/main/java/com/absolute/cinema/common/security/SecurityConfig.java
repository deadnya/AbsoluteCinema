package com.absolute.cinema.common.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()

                        .requestMatchers("/users/me").authenticated()
                        .requestMatchers("/users/{id}").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/films").permitAll()
                        .requestMatchers(HttpMethod.GET, "/films/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/films").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/films/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/films/{id}").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/films/{id}/reviews").permitAll()
                        .requestMatchers(HttpMethod.POST, "/films/{id}/reviews").authenticated()
                        .requestMatchers(HttpMethod.GET, "/reviews/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/reviews/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/reviews/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/halls").permitAll()
                        .requestMatchers(HttpMethod.GET, "/halls/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/halls").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/halls/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/halls/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/halls/{id}/plan").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/halls/{id}/plan").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/sessions").permitAll()
                        .requestMatchers(HttpMethod.POST, "/sessions").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/sessions/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/sessions/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/sessions/{id}").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/seat-categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/seat-categories/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/seat-categories").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/seat-categories/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/seat-categories/{id}").hasAuthority("ADMIN")

                        .requestMatchers("/sessions/{sessionId}/tickets").permitAll()
                        .requestMatchers("/tickets/{id}/reserve").authenticated()
                        .requestMatchers("/tickets/{id}/cancel-reservation").authenticated()

                        .requestMatchers("/purchases/**").authenticated()

                        .requestMatchers("/payments/**").authenticated()

                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/api-docs.yaml").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}