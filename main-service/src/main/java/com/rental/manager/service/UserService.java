package com.rental.manager.service;

import com.rental.manager.dto.requestdto.UserPatchRequestDTO;
import com.rental.manager.dto.requestdto.UserRequestDTO;
import com.rental.manager.dto.responsedto.UserResponseDTO;


import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO updateUser(UUID id, UserRequestDTO request);
    UserResponseDTO patchUser(UUID id, UserPatchRequestDTO request);
    void deleteUser(UUID id);

    UserResponseDTO getUserById(UUID id);
    List<UserResponseDTO> getUserByName(String name);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByPhoneNumber(String phoneNumber);
}
