package com.baerchen.central.authentication.userregister.boundary;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @Email String email,
        @Size(max = 255) String info,
        List<@Size(min = 1) String> backend,
        Set<@NotBlank String> roles
) {}