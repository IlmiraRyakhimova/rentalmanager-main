package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.security.jwt.JwtService;
import com.rental.manager.security.jwt.dto.JwtDTO;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO signUp(SignUpRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        AuthResponseDTO response = new AuthResponseDTO();
        JwtDTO jwtDto = jwtService.generateAuthToken(user.getEmail());
        user.setRefreshTokenHash(jwtService.hashRefreshToken(jwtDto.getRefreshToken()));
        userRepository.save(user);

        response.setAccessToken(jwtDto.getToken());
        response.setRefreshToken(jwtDto.getRefreshToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public AuthResponseDTO signIn(SignInRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        AuthResponseDTO response = new AuthResponseDTO();
        JwtDTO jwtDto = jwtService.generateAuthToken(user.getEmail());
        user.setRefreshTokenHash(jwtService.hashRefreshToken(jwtDto.getRefreshToken()));
        userRepository.save(user);

        response.setAccessToken(jwtDto.getToken());
        response.setRefreshToken(jwtDto.getRefreshToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    public AuthResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String refreshToken = refreshTokenDTO.getRefreshToken();

        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh token expired or invalid");
        }

        User user = userRepository.findByEmail(jwtService.extractEmail(refreshToken));

        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        if (!jwtService.matchesRefreshToken(refreshToken, user.getRefreshTokenHash())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        JwtDTO jwtDto = jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(jwtDto.getToken());
        response.setRefreshToken(jwtDto.getRefreshToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
}
