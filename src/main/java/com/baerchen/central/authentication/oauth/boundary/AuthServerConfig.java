package com.baerchen.central.authentication.oauth.boundary;

import com.baerchen.central.authentication.oauth.control.RolesClaimConverter;
import com.baerchen.central.authentication.userregister.boundary.RegistrationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

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
public class AuthServerConfig {
    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception{
        // Configures endpoints like /oauth2/authorize, /oauth2/token, /.well-known/openid-configuration, etc.
        http
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers(RegistrationController.REGISTRATION_ENDPOINT).permitAll()
                        /**
                         * alternative:
                         *  Method security (class or method level)
                         * @PreAuthorize("hasRole('ADMIN')")
                         * public void adminOnlyAction() { ... }
                         * */
                        // executed this once in order to create a client
                       .requestMatchers("/admin/**").permitAll()
                  //      .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                //.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt( jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()
                ))

                )
                //.csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/token", RegistrationController.REGISTRATION_ENDPOINT));
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/token", RegistrationController.REGISTRATION_ENDPOINT,"/admin/**"));

        return http.build();

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

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new RolesClaimConverter());
        return converter;
    }

}
