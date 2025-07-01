package com.baerchen.central.authentication.userregister.boundary;

import com.baerchen.central.authentication.userregister.control.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class RegistrationController {

    private final CustomUserDetailsService service;

    public static final String REGISTRATION_ENDPOINT = "/register";

    @PostMapping
    public  ResponseEntity<?> register(@RequestBody RegisterRequest request){
        RegisterResult result = service.register(request);
        return ResponseEntity.status(result.success()  ? 201 : 400).body(result.message());
    }



}
