package com.baerchen.central.authentication.registeredclient.control;

import com.baerchen.central.authentication.registeredclient.boundary.RegisteredClientDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.List;
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
        this.customRegisteredClientRepo.updateClientByClientId(this.mapper.toEntityForUpdate(dto, this.encoder));
        return dto;
    }

    public RegisteredClientDTO updateById(RegisteredClientDTO dto){
        getById(dto.id()).orElseThrow();
        log.info("Client [{}] found proceeding to the update.", dto.id());
        this.repo.save(this.mapper.toEntityForUpdate(dto, this.encoder));
        return dto;
    }

    public void deleteByClientId(String clientId) {
        this.customRegisteredClientRepo.deleteClientByClientId(clientId);
    }

    public List<RegisteredClientDTO> getClients() {
        return this.customRegisteredClientRepo.listAllClients();
    }
}
