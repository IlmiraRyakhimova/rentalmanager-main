package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.security.jwt.JwtService;
import com.rental.manager.security.jwt.dto.JwtDTO;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;
import com.rental.manager.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
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
        userRepository.save(user);

        emailService.createAndSendVerificationToken(user.getEmail());


        AuthResponseDTO response = new AuthResponseDTO();
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

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Пожалуйста, подтвердите ваш email, чтобы войти в систему.");
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
    public AuthResponseDTO refreshAccessToken(RefreshTokenDTO refreshTokenDTO) {
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

    public void logOut() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshTokenHash(null);
            userRepository.save(user);
        }
    }
}
