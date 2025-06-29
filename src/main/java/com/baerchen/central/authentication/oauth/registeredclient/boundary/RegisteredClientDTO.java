package com.baerchen.central.authentication.oauth.registeredclient.boundary;

import java.util.Set;

public record RegisteredClientDTO(
        String id,
        String clientId,
        String clientSecret,
        Set<String> redirectUris,
        Set<String> scopes,
        Set<String> grantTypes
) {}