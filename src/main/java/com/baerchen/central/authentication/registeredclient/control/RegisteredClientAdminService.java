package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;
import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class RegisteredClientAdminService {

    private final JdbcRegisteredClientRepository repo;
    private final PasswordEncoder encoder;
    private final CustomRegisteredClientRepo customRegisteredClientRepo;
    private final RegisteredClientMapper mapper;

    public RegisteredClientDTO create(RegisteredClientDTO dto){
        dto = sanitizeRegisteredClientDtoForPost(dto);
        RegisteredClient rc = this.mapper.toEntity(dto,this.encoder);
        this.repo.save(rc);
        return dto;
    }

    public Optional<RegisteredClientDTO> getByClientId(String clientId) {
        RegisteredClient rc = this.repo.findByClientId(clientId);
        if (rc == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mapper.toDto(rc));
    }

    public Optional<RegisteredClientDTO> getById(String clientId) {
        RegisteredClient rc = this.repo.findById(clientId);
        if (rc == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mapper.toDto(rc));
    }

    public RegisteredClientDTO updateByClientId(RegisteredClientDTO dto){
        getByClientId(dto.clientId()).orElseThrow();
        log.info("Client [{}] found proceeding to the update.", dto.clientId());
        this.customRegisteredClientRepo.updateClientByClientId(this.mapper.toEntityForUpdate(dto));
        return dto;
    }

    public RegisteredClientDTO updateById(RegisteredClientDTO dto){
        getById(dto.id()).orElseThrow();
        log.info("Client [{}] found proceeding to the update.", dto.id());
        this.repo.save(this.mapper.toEntityForUpdate(dto));
        return dto;
    }

    public void deleteByClientId(String clientId) {
        this.customRegisteredClientRepo.deleteClientByClientId(clientId);
    }

    private RegisteredClientDTO sanitizeRegisteredClientDtoForPost(RegisteredClientDTO dto){
        return new RegisteredClientDTO(
                null,
                dto.clientId(),
                dto.clientSecret(),
                dto.redirectUris()==null ? Set.of("localhost:9090/login") : dto.redirectUris(),
                dto.scopes()==null ? Set.of("openid", "profile", "email", "read", "write") : dto.scopes(),
                dto.authenticationMethods()==null ? Set.of("none") : dto.authenticationMethods(),
                dto.grantTypes()==null ? Set.of("client_credentials", "authorization_code") : dto.grantTypes()
        );
    }

}
