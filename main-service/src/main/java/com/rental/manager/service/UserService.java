package com.rental.manager.service;

import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser(User user);
    UserResponseDTO updateUser(UUID id, User newUserInfo);
    void deleteUser(UUID id);

    UserResponseDTO getUserById(UUID id);
    List<UserResponseDTO> getUserByName(String name);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByPhoneNumber(String phoneNumber);
}
