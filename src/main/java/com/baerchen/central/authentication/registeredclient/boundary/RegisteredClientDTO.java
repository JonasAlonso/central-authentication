package com.baerchen.central.authentication.registeredclient.boundary;

import com.baerchen.central.authentication.runtime.control.Cleanable;

import java.util.Set;

public record RegisteredClientDTO(
        String id,
        String clientId,
        String clientSecret,
        Set<String> redirectUris,
        Set<String> scopes,
        Set<String> authenticationMethods,
        Set<String> grantTypes
) implements Cleanable<RegisteredClientDTO> {
    @Override
    public RegisteredClientDTO cleaned() {
        return new RegisteredClientDTO(
                id,
                clientId,
                clientSecret,
                cleanSet(redirectUris),
                cleanSet(scopes),
                cleanSet(authenticationMethods),
                cleanSet(grantTypes)
        );
    }
}