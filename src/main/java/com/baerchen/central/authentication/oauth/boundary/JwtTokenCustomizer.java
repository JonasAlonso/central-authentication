package com.baerchen.central.authentication.oauth.boundary;

import com.baerchen.central.authentication.userregister.control.UserRepository;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(UserRepository userRepository){
        return context -> Optional.ofNullable(context.getPrincipal())
                .map(principal -> principal.getName())
                .flatMap(userRepository::findByUsername)
                .ifPresent(user -> context.getClaims().claim("roles", user.getRoles()));
    }


}
