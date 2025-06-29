package com.baerchen.central.authentication.oauth.control;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RolesClaimConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = source.getClaimAsStringList("roles");
        if (roles != null){
            for (String role: roles){
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
            }
        }
        return authorities;
    }
}
