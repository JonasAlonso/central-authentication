package com.baerchen.central.authentication.client.boundary;

import java.util.Set;

public record RegisteredClientDTO(
        String id,
        String clientId,
        String clientSecret,
        Set<String> redirectUris,
        Set<String> scopes,
        Set<String> authenticationMethods,
        Set<String> grantTypes
) {}