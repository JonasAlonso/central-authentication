package com.baerchen.central.authentication.oauth.registeredclient.boundary;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminHello() {
        return "Hello, Admin!";
    }
}