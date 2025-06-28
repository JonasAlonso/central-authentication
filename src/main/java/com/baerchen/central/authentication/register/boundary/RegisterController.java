package com.baerchen.central.authentication.register.boundary;

import com.baerchen.central.authentication.register.control.RegisterUserControl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@AllArgsConstructor
@RequestMapping(RegistrationController.REGISTRATION_ENDPOINT)
public class RegisterController {

    private final RegisterUserControl control;

    public  ResponseEntity<?> register(@RequestBody RegisterRequest request){
        RegisterResult result = control.register(request);
        return ResponseEntity.status(Arrays.asList(200,201).contains(result.success())  ? 201 : 400).body(result.message());
    }

}
