package com.baerchen.central.authentication.user.boundary;

import com.baerchen.central.authentication.user.control.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/register")
@AllArgsConstructor
@Tag(name = "User Registration", description = "Operations for public user registration")
public class RegistrationController {

    private final CustomUserDetailsService service;

    public static final String REGISTRATION_ENDPOINT = "/register";

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation or duplication error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public  ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request){
        log.info("Registering new user: {}", request.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.create(request));

    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Deleting user by id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        log.info("Updating user [{}] with new values: {}", id, request);
        return ResponseEntity.ok(service.update(id, request));
    }



}
