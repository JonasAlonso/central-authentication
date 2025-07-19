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
        return ResponseEntity.ok(this.service.create(dto));
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
        String client = this.service.getByClientId(dto.clientId()).map(RegisteredClientDTO::clientId).stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Client not found: [%s]", dto.clientId()))
        );
        return ResponseEntity.ok(this.service.updateByClientId(dto));
    }

    public ResponseEntity<RegisteredClientDTO> updateById(@RequestBody RegisteredClientDTO dto){
        String client = this.service.getById(dto.clientId()).map(RegisteredClientDTO::id).stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Client not found: [%s]", dto.clientId()))
        );
        return ResponseEntity.ok(this.service.updateById(dto));
    }

}
