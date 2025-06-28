package com.baerchen.central.authentication.register.boundary;

import com.baerchen.central.authentication.runtime.control.UserRepository;
import com.baerchen.central.authentication.runtime.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RegistrationController {

    public static final String REGISTRATION_ENDPOINT = "/register";
    private UserRepository repo;

    private PasswordEncoder encoder;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return ResponseEntity.ok("User registered");

    }


}
