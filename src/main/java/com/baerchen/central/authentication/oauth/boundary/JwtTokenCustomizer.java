package com.baerchen.central.authentication.oauth.boundary;

import com.baerchen.central.authentication.userregister.control.UserRepository;
import com.baerchen.central.authentication.userregister.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(UserRepository userRepository){
        return context -> {
            if (context.getPrincipal() != null && context.getPrincipal().getName()!=null) {
                String username = context.getPrincipal().getName();
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null){
                    context.getClaims().claim("roles", user.getRoles());
                }
            }
        };
    }


}
