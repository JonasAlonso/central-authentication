package com.baerchen.central.authentication.runtime.entity;

import java.util.Set;
import java.util.stream.Collectors;

public interface Defaults {
    Set<String> DEFAULT_GRANTS = Set.of("client_credentials", "authorization_code");
    Set<String> DEFAULT_SCOPES = Set.of("openid", "profile", "email", "read", "write");
    Set<String> DEFAULT_REDIRECT_URIS = Set.of("http://localhost:9090/login");
    Set<String> DEFAULT_AUTH_METHODS = Set.of("none");

    default Set<String> defaultSet(Set<String> input, Set<String> fallback) {
        if (input == null || input.stream().allMatch(s -> s == null || s.isBlank())) {
            return fallback;
        }
        return input.stream().map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toSet());
    }

}
