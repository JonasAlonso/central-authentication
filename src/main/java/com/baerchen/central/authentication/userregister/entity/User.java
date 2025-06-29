package com.baerchen.central.authentication.userregister.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuario")
public class User {

    @Id
    @Generated
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // hashed

    @Column(nullable = false)
    private String email;

    private String info; // general information about the user
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @CollectionTable(name = "user_backends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "backend_id")
    private List<String> backend;

    private Instant createdAt;

    private Instant lastLogin;

    private Instant lastLogout;

}
