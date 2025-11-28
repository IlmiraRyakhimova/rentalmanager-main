package com.rental.manager.security.auth;

import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO signUp(SignUpRequestDTO request);
    AuthResponseDTO signIn(SignInRequestDTO request);

}
