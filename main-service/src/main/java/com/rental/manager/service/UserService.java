package com.rental.manager.service;

import com.rental.manager.dto.requestdto.ChangePasswordRequestDto;
import com.rental.manager.dto.requestdto.UserPatchRequestDto;
import com.rental.manager.dto.requestdto.UserRequestDto;
import com.rental.manager.dto.responsedto.UserResponseDto;


import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);
    UserResponseDto updateUser(UUID id, UserRequestDto request);
    UserResponseDto patchUser(UUID id, UserPatchRequestDto request);
    void deleteUser(UUID id);

    UserResponseDto getUserById(UUID id);
    List<UserResponseDto> getUserByName(String name);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserByEmail(String email);
    UserResponseDto getUserByPhoneNumber(String phoneNumber);
    void changePassword(ChangePasswordRequestDto request);
}
