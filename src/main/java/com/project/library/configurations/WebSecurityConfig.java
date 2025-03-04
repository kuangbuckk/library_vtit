package com.project.library.configurations;

import com.project.library.entities.RoleGroup;
import com.project.library.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFilter jwtTokenFilter;
    private final String[] bypassRequests = {
            //Swagger
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "swagger-ui.html",
            "/api-docs"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( request ->
                        request
                                .requestMatchers(GET,
                                        String.format("%s/categories/**", apiPrefix)).permitAll()
                                .requestMatchers(GET,
                                        String.format("%s/books/**", apiPrefix)).permitAll()
                                .requestMatchers(GET,
                                        String.format("%s/posts/**", apiPrefix)).permitAll()
                                .requestMatchers(GET,
                                        String.format("%s/comments/**", apiPrefix)).permitAll()
                                .requestMatchers(GET,
                                        String.format("%s/role_groups/**", apiPrefix)).permitAll()
                                .requestMatchers(POST,
                                        String.format("%s/users/register", apiPrefix)).permitAll()
                                .requestMatchers(POST,
                                        String.format("%s/users/login", apiPrefix)).permitAll()
                                .requestMatchers(POST,
                                        String.format("%s/users/logout", apiPrefix)).permitAll()
                                .requestMatchers(POST,
                                        String.format("%s/users/refresh-token", apiPrefix)).permitAll()
                                .requestMatchers(GET,
                                        String.format("%s/http-client/**", apiPrefix)).permitAll()
                                .requestMatchers(bypassRequests).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
