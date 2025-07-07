package com.baerchen.central.authentication.userregister.control;

import com.baerchen.central.authentication.userregister.boundary.RegisterRequest;
import com.baerchen.central.authentication.userregister.boundary.RegisterResult;
import com.baerchen.central.authentication.userregister.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
}
