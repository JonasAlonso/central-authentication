package com.baerchen.central.authentication.register.boundary;

public record RegisterRequest(String username, String password, String email) {}
