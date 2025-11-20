package com.rental.manager.service;

import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser(User user);
    UserResponseDTO updateUser(UUID id, User newUserInfo);
    UserResponseDTO updateUser(UUID id, String name, String email, String phoneNumber);
    void deleteUser(UUID id);

    UserResponseDTO getUserById(UUID id);
    List<UserResponseDTO> getUserByName(String name);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByPhoneNumber(String phoneNumber);
}
