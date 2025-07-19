package com.baerchen.central.authentication.user.control;

import com.baerchen.central.authentication.user.boundary.RegisterRequest;
import com.baerchen.central.authentication.user.boundary.RegisterResult;
import com.baerchen.central.authentication.user.boundary.UserDTO;
import com.baerchen.central.authentication.user.boundary.UserUpdateRequest;
import com.baerchen.central.authentication.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repo.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("User not found by username"));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);

    }

    public RegisterResult register(RegisterRequest req) {
        if (this.repo.existsByUsername(req.username())) {
            return new RegisterResult(false, "Username already exists");
        }

        if (this.repo.existsByEmail(req.email())) {
            return new RegisterResult(false,String.format("The email provided for user [%s] is already in use", req.username()));
        }
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setEmail(req.email());
        user.setRoles(Set.of("USER"));
        repo.save(user);
        return new RegisterResult(true, "Registration successful");
    }

    public Optional<UserDTO> getById(UUID id) {
        return repo.findById(id).map(this::toDto);
    }

    public void deleteById(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        repo.deleteById(id);
    }

    public UserDTO create(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists: " + req.username());
        }

        if (repo.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use: " + req.email());
        }

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setEmail(req.email());
        user.setRoles(Set.of("USER"));
        return toDto(repo.save(user));
    }

    public UserDTO update(UUID id, UserUpdateRequest update) {
        User user = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));

        if (update.email() != null) user.setEmail(update.email());
        if (update.info() != null) user.setInfo(update.info());
        if (update.backend() != null) user.setBackend(update.backend());
        if (update.roles() != null) user.setRoles(update.roles());

        return toDto(repo.save(user));
    }

    private UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user.getBackend(),
                user.getInfo()
        );
    }
}
