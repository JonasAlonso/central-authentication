package com.baerchen.central.authentication.oauth.boundary;

import com.baerchen.central.authentication.register.boundary.RegistrationController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

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
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception{
        // Configures endpoints like /oauth2/authorize, /oauth2/token, /.well-known/openid-configuration, etc.
        http
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers(RegistrationController.REGISTRATION_ENDPOINT).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/token", RegistrationController.REGISTRATION_ENDPOINT));

        return http.build();

    }

    @Bean
    @Qualifier("app01-client-auth")
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder){
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("app01-client") //custom client apps
                .clientSecret(passwordEncoder.encode("app01-secret"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8081/login/oauth2/code/app01-client") //also custom for the client app
                .scope(OidcScopes.OPENID)
                .scope("read")
                .scope("write")
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
