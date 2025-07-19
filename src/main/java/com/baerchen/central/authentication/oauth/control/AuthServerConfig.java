package com.baerchen.central.authentication.oauth.control;

import com.baerchen.central.authentication.user.boundary.RegistrationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * This config sets up:
 * The authorization endpoints (/oauth2/authorize, /oauth2/token)
 *
 * OpenID Connect
 *
 * A hardcoded demo client for local testing
 * (replace with JDBC-registered clients for production)
 */

@Configuration
public class AuthServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .securityMatcher("/oauth2/**", "/.well-known/**") // ✅ properly scoped
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults());

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/**")); // optional

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/admin/**",
                                "/client-admin.html",
                                "/debug.html",
                                "/error",
                                RegistrationController.REGISTRATION_ENDPOINT
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/**", "/error", RegistrationController.REGISTRATION_ENDPOINT));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JdbcRegisteredClientRepository registeredClientRepository(DataSource dataSource) {
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RolesClaimConverter());
        return converter;
    }

}
