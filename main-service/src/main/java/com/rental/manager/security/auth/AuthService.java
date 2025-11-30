package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;

public interface AuthService {
    AuthResponseDTO signUp(SignUpRequestDTO request);
    AuthResponseDTO signIn(SignInRequestDTO request);
    AuthResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

}
