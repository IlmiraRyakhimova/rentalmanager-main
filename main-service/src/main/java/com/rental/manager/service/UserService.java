package com.rental.manager.service;

import com.rental.manager.entities.User;
import com.rental.manager.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(UserRole role);
    User createUser(User user);
    User updateUser(Long id, User newUserInfo);
    void deleteUser(Long id);
}
