package com.baerchen.central.authentication.runtime.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuario")
public class User {

    @Id
    @Generated
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password; // hashed
    private String email;
    private String info; // general information about the user
    private String role = "USER"; // expand

    @CollectionTable(name = "user_backends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "backend_id")
    private List<String> backend;

    private Instant createdAt;

    private Instant lastLogin;

    private Instant lastLogout;

}
