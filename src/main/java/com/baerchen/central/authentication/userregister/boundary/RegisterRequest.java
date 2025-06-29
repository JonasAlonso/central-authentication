package com.baerchen.central.authentication.userregister.boundary;

public record RegisterRequest(String username, String password, String email) {}
