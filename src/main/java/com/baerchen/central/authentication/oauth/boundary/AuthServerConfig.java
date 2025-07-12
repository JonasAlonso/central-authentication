package com.baerchen.central.authentication.oauth.boundary;

import com.baerchen.central.authentication.oauth.control.RolesClaimConverter;
import com.baerchen.central.authentication.userregister.boundary.RegistrationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;

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
@EnableWebFluxSecurity
public class AuthServerConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
           return  http
                    .authorizeExchange(exchanges -> exchanges
                            .pathMatchers(RegistrationController.REGISTRATION_ENDPOINT).permitAll()
                            .pathMatchers("/admin/**").hasRole("ADMIN")
                            .anyExchange().authenticated()
                    )
                    // .formLogin() is NOT supported in Spring Cloud Gateway!
                    // .formLogin(withDefaults()) // Only for reactive web *apps*, NOT gateway!
                   .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
                    // CSRF in WebFlux: Use matcher objects or simply disable as below
                    .csrf(ServerHttpSecurity.CsrfSpec::disable).build();
        }


    /**
     *  needed only to try a hardcoded client as example
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
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JdbcRegisteredClientRepository registeredClientRepository(DataSource dataSource) {
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

}
