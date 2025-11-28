package com.rental.manager.security.jwt;


public interface JwtService {

    String generateToken(String email);
    String extractEmail(String token);
    boolean validateToken(String token);
}
