package com.baerchen.central.authentication.register.control;

import com.baerchen.central.authentication.register.boundary.RegisterRequest;
import com.baerchen.central.authentication.register.boundary.RegisterResult;
import com.baerchen.central.authentication.runtime.control.UserRepository;
import com.baerchen.central.authentication.runtime.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RegisterUserControl {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public RegisterResult register(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) {
            return new RegisterResult(false, "Username already exists");
        }
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setEmail(req.email());
        repo.save(user);
        return new RegisterResult(true, "Registration successful");
    }
}
