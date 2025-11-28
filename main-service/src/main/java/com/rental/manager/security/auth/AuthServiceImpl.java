package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
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

        User savedUser = userRepository.save(user);
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(jwtService.generateToken(savedUser.getEmail()));
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    @Override
    public AuthResponseDTO signIn(SignInRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(jwtService.generateToken(user.getEmail()));
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

}
