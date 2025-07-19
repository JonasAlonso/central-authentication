package com.baerchen.central.authentication.user.boundary;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        Set<String> roles,
        List<String> backend,
        String info
) {}

