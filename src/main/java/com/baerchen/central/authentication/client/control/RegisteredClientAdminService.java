package com.baerchen.central.authentication.client.control;

import com.baerchen.central.authentication.client.boundary.RegisteredClientDTO;
import com.baerchen.central.authentication.client.boundary.RegisteredClientMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RegisteredClientAdminService {

    private final JdbcRegisteredClientRepository repo;
    private final PasswordEncoder encoder;
    private final CustomRegisteredClientRepo customRegisteredClientRepo;
    private final RegisteredClientMapper mapper;

    public RegisteredClientDTO create(RegisteredClientDTO dto){
        RegisteredClient rc = this.mapper.toEntity(dto,this.encoder);
        /*
        RegisteredClient rc = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(dto.clientId())
                .clientSecret(this.encoder.encode(dto.clientSecret()))
                .redirectUris(uris -> uris.addAll(dto.redirectUris()))
                .scopes(scopes -> scopes.addAll(dto.scopes()))
                .authorizationGrantTypes(grants -> dto.grantTypes().forEach(g -> grants.add(new AuthorizationGrantType(g))))
                .build();*/
        repo.save(rc);
        return dto;
    }

    public Optional<RegisteredClientDTO> get(String clientId) {
        RegisteredClient rc = this.repo.findByClientId(clientId);
        if (rc == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mapper.toDto(rc));
    }

    public RegisteredClientDTO update(RegisteredClientDTO dto){
        get(dto.clientId()).orElseThrow();
        log.info("Client [{}] found proceeding to the update.", dto.clientId());
        this.repo.save(this.mapper.toEntity(dto, this.encoder));
        return dto;
    }



    public void deleteByClientId(String clientId) {
        this.customRegisteredClientRepo.deleteClientByClientId(clientId);
    }



}
