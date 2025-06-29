package com.baerchen.central.authentication.oauth.registeredclient.control;

import com.baerchen.central.authentication.oauth.registeredclient.boundary.RegisteredClientDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisteredClientAdminService {

    private final JdbcRegisteredClientRepository repo;
    private final PasswordEncoder encoder;
    private final CustomRegisteredClientRepo customRegisteredClientRepo;

    public RegisteredClientDTO create(RegisteredClientDTO dto){
        RegisteredClient rc = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(dto.clientId())
                .clientSecret(this.encoder.encode(dto.clientSecret()))
                .redirectUris(uris -> uris.addAll(dto.redirectUris()))
                .scopes(scopes -> scopes.addAll(dto.scopes()))
                .authorizationGrantTypes(grants -> dto.grantTypes().forEach(g -> grants.add(new AuthorizationGrantType(g))))
                .build();
        repo.save(rc);
        return dto;
    }

    public RegisteredClientDTO get(String clientId) {
        RegisteredClient rc = repo.findByClientId(clientId);
        if (rc == null) return null;
        return toDTO(rc);
    }

    private RegisteredClientDTO toDTO(RegisteredClient rc) {
        return new RegisteredClientDTO(
                rc.getId(),
                rc.getClientId(),
                rc.getClientSecret(),
                rc.getRedirectUris(),
                rc.getScopes(),
                rc.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(java.util.stream.Collectors.toSet())
        );
    }
    public void deleteByClientId(String clientId) {
        customRegisteredClientRepo.deleteClientByClientId(clientId);
    }




}
