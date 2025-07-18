package com.baerchen.central.authentication.client.boundary;

import com.baerchen.central.authentication.client.control.RegisteredClientAdminService;
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
        return service.get(clientId)
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

    @PutMapping
    public ResponseEntity<RegisteredClientDTO> update(@RequestBody RegisteredClientDTO dto){
        return ResponseEntity.ok(this.service.update(dto));
    }

}
