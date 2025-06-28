package com.baerchen.central.authentication.runtime.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.baerchen.central.authentication.runtime.entity.User;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
