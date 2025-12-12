package com.rental.manager.security.jwt;

import com.rental.manager.security.jwt.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtDTO generateAuthToken(String email) {
        JwtDTO jwtDto = new JwtDTO();
        jwtDto.setToken(generateToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return jwtDto;
    }

    public String generateToken(String email) {
        long currentMillis = System.currentTimeMillis();
        long currentSeconds = currentMillis / 1000;
        Date date = new Date(currentMillis + expiration);
        System.out.println("=== JWT Generation Debug ===");
        System.out.println("System.currentTimeMillis(): " + currentMillis);
        System.out.println("Current Date: " + new Date(currentMillis));
        System.out.println("Unix timestamp (seconds): " + currentSeconds);
        System.out.println("Expiration ms: " + expiration);
        System.out.println("Expiration Date: " + date);
        System.out.println("Email: " + email);
        System.out.println("===========================");
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date date = new Date(System.currentTimeMillis() + (expiration * 4));
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtDTO refreshBaseToken(String email, String refreshToken) {
        JwtDTO jwtDto = new JwtDTO();
        jwtDto.setToken(generateToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }



    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not found", e);
        }
    }


    public boolean matchesRefreshToken(String refreshToken, String hashedToken) {
        String hash = hashRefreshToken(refreshToken);
        return hash.equals(hashedToken);
    }
}


