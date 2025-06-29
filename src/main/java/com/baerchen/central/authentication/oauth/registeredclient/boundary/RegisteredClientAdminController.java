package com.baerchen.central.authentication.oauth.registeredclient.boundary;

import com.baerchen.central.authentication.oauth.registeredclient.control.RegisteredClientAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        var client = this.service.get(clientId);
        return (client != null) ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@PathVariable String clientId) {
        this.service.deleteByClientId(clientId);
        return ResponseEntity.noContent().build();
    }

}
