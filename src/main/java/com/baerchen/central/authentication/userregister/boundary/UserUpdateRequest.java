package com.baerchen.central.authentication.userregister.boundary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record UserUpdateRequest(
        @Email String email,
        @Size(max = 255) String info,
        List<@Size(min = 1) String> backend,
        Set<@NotBlank String> roles
) {}