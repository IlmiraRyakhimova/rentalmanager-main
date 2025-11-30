package com.rental.manager.security.jwt;


import com.rental.manager.security.jwt.dto.JwtDTO;

public interface JwtService {

    JwtDTO generateAuthToken(String email);
    String generateToken(String email);
    String generateRefreshToken(String email);
    JwtDTO refreshBaseToken(String email, String refreshToken);
    String extractEmail(String token);
    boolean validateToken(String token);
    String hashRefreshToken(String refreshToken);
    boolean matchesRefreshToken(String refreshToken, String hashedToken);
}
