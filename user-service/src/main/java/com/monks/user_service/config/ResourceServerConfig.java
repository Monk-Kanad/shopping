package com.monks.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity  // Explicitly enable reactive security (good practice)
public class ResourceServerConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                // 1. Authorization rules (reactive style)
                .authorizeExchange(exchanges -> exchanges
                        // Optional: permit health/actuator if needed
                        .pathMatchers("/actuator/**").permitAll()
                        // Protect everything else
                        .anyExchange().authenticated()
                )

                // 2. Enable JWT validation as resource server
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())   // Uses issuer-uri from properties
                )
                // Common for APIs: disable unnecessary features
                .csrf(ServerHttpSecurity.CsrfSpec::disable)   // Usually safe for pure APIs
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        return http.build();
    }

    @Bean
    ReactiveJwtDecoder reactiveJwtDecoder(){
        return new NimbusReactiveJwtDecoder("http://localhost:9090/oauth2/jwks");
    }
}
