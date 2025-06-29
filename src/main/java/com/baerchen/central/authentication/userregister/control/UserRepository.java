package com.baerchen.central.authentication.userregister.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.baerchen.central.authentication.userregister.entity.User;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
