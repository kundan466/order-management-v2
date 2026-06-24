package com.order_management.demo.config;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {
    @Autowired
private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder
        passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {
        log.info("Configuring security filter chain");
        
        //expect auth 
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
            session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS
            ))
            .authorizeHttpRequests(auth -> auth
            //public 
            .requestMatchers("/api/users/auth/**")
            .permitAll()

            //inventory 
            .requestMatchers("/api/inventory/**")
            .hasRole("ADMIN")

            //order
            .requestMatchers(HttpMethod.POST, "/api/orders/**")
            .hasRole("CUSTOMER")

            .requestMatchers("/api/orders/**")
            .hasAnyRole("ADMIN","CUSTOMER")

            //payment
            .requestMatchers(HttpMethod.POST, "/api/payments/**")
            .hasRole("CUSTOMER")

            .requestMatchers(HttpMethod.GET, "/api/payments/**")
            .hasRole("ADMIN")

            //product
            .requestMatchers(HttpMethod.POST, "/api/products/**")
            .hasRole("ADMIN")

            .requestMatchers(HttpMethod.PUT, "/api/products/**")
            .hasRole("ADMIN")

            .requestMatchers(HttpMethod.DELETE, "/api/products/**")
            .hasRole("ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/products/**")
            .hasAnyRole("ADMIN", "CUSTOMER")

            //user
            .requestMatchers(HttpMethod.GET, "/api/users/**")
            .hasRole("ADMIN")

            .requestMatchers(HttpMethod.PUT, "/api/users/**")
            .hasAnyRole("ADMIN", "CUSTOMER")
            
            .requestMatchers(HttpMethod.DELETE, "/api/users/**")
            .hasAnyRole("ADMIN", "CUSTOMER")
           
            .requestMatchers(HttpMethod.PATCH, "/api/users/**")
            .hasAnyRole("ADMIN", "CUSTOMER")

            .anyRequest()
            .authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}