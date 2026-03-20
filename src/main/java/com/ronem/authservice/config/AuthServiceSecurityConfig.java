/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:27/01/2026
 * Time:15:05
 */


package com.ronem.authservice.config;

import com.ronem.rupiasecuritylib.constants.PublicPaths;
import com.ronem.rupiasecuritylib.filter.GatewayAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class AuthServiceSecurityConfig {

    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        log.info("========================================");
        log.info("Configuring Security Filter Chain");
        log.info("CSRF: DISABLED");
        log.info("Session: STATELESS");
        log.info("========================================");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionConfig ->
                                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    PublicPaths.AUTH_PUBLIC_PATHS.forEach(path ->
                            auth.requestMatchers(path).permitAll()
                    );
                    PublicPaths.COMMON_PUBLIC_PATHS.forEach(path ->
                            auth.requestMatchers(path).permitAll()
                    );

                    //internal endpoints
                    auth.requestMatchers("/auth/internal/**").permitAll();

                    // else authenticate
                    auth.anyRequest().authenticated();

                })
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint((request, response, authException) ->
                                        handlerExceptionResolver.resolveException(request, response, null, authException))
                                .accessDeniedHandler((request, response, accessDeniedException) ->
                                        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
                )
                .addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }

}