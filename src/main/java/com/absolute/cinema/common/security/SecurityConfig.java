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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
//@SecurityScheme(type = SecuritySchemeType.HTTP, name = "bearerAuth", scheme = "bearer")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

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

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        return http.build();
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