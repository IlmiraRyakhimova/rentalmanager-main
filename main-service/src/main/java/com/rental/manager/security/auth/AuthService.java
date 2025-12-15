package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDto;
import com.rental.manager.dto.requestdto.SignUpRequestDto;
import com.rental.manager.dto.responsedto.AuthResponseDto;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;

public interface AuthService {
    AuthResponseDto signUp(SignUpRequestDto request);
    AuthResponseDto signIn(SignInRequestDto request);
    AuthResponseDto refreshAccessToken(RefreshTokenDTO refreshTokenDTO);
    void logOut();

}
