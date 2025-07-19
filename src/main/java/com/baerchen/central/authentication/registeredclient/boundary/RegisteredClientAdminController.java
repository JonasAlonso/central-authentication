package com.baerchen.central.authentication.registeredclient.boundary;

import com.baerchen.central.authentication.registeredclient.control.RegisteredClientAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
/**
 * For production, protect /admin/clients/**—
 * Only allow access to users with an ADMIN role (you can use method security or a custom filter).
 *
 * For now, in dev,permit all, but never in prod.
 */
@RequestMapping("/admin/clients")
public class RegisteredClientAdminController {

    private final RegisteredClientAdminService service;

    public RegisteredClientAdminController(RegisteredClientAdminService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RegisteredClientDTO> create(@RequestBody RegisteredClientDTO dto) {
        return ResponseEntity.ok(this.service.create(dto.cleaned()));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<RegisteredClientDTO> get(@PathVariable String clientId) {
        return service.getByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Client not found: [%s]",clientId))
                );
    }
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@PathVariable String clientId) {
        this.service.deleteByClientId(clientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<RegisteredClientDTO> updateByClientId(@RequestBody RegisteredClientDTO dto){
        this.service.getByClientId(dto.clientId()).map(RegisteredClientDTO::clientId).stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Client not found: [%s]", dto.clientId()))
        );
        return ResponseEntity.ok(this.service.updateByClientId(dto));
    }


    @PutMapping("/id/{id}")
    public ResponseEntity<RegisteredClientDTO> updateById(@PathVariable String id, @RequestBody RegisteredClientDTO dto){
        this.service.getById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Client not found: [%s]", id))
        );
        RegisteredClientDTO updated = new RegisteredClientDTO(
                id,
                dto.clientId(),
                dto.clientSecret(),
                dto.redirectUris(),
                dto.scopes(),
                dto.authenticationMethods(),
                dto.grantTypes(),
                dto.clientSettings()
        );
        return ResponseEntity.ok(this.service.updateById(updated));
    }

}
