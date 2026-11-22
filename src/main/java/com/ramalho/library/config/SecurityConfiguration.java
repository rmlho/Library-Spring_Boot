package com.ramalho.library.config;

import com.ramalho.library.exception.ErroResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring Security Filter Chain...");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/register").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/v1/rent",
                                "/v1/rent/*/return"
                        ).hasAnyRole("READER", "LIBRARY", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/rent/rentsUser")
                        .hasAnyRole("READER", "LIBRARY", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/v1/rent/{id}")
                        .hasAnyRole("READER", "LIBRARY", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/books")
                        .hasAnyRole("LIBRARY", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/books", "/v1/books/**")
                        .hasAnyRole("LIBRARY", "ADMIN", "READER")
                        .requestMatchers(HttpMethod.PATCH, "/v1/auth/{id}/role/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/v1/fine/{id}")
                        .hasAnyRole("LIBRARY", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/fine/{rentId}")
                        .hasAnyRole("LIBRARY", "ADMIN", "READER")
                        .requestMatchers(HttpMethod.GET, "/v1/fine")
                        .hasAnyRole("LIBRARY", "ADMIN")
                        .requestMatchers(
                                "/v1/author", "/v1/author/**",
                                "/v1/publisher", "/v1/publisher/**"
                        )
                        .hasAnyRole("LIBRARY", "ADMIN")
                        .anyRequest().hasAnyRole("LIBRARY", "ADMIN")
                )

                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");

                            objectMapper.writeValue(response.getOutputStream(),
                                    ErroResponse.builder()
                                            .message("Authentication Required")
                                            .status(HttpStatus.UNAUTHORIZED.value())
                                            .build());
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");

                            objectMapper.writeValue(response.getOutputStream(),
                                    ErroResponse.builder()
                                            .message("Access Denied")
                                            .status(HttpStatus.FORBIDDEN.value())
                                            .build());
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
