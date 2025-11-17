package com.rental.manager.service.impl;

import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.rental.manager.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserResponseDTO createUser(User user) {
        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UUID id, User newUserInfo) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(newUserInfo.getName());
        user.setEmail(newUserInfo.getEmail());
        user.setPhoneNumber(newUserInfo.getPhoneNumber());
        return mapper.toDTO(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return mapper.toDTO(userRepository.findById(id).orElseThrow());
    }

    public List<UserResponseDTO> getUserByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream().map(mapper::toDTO).toList();
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return mapper.toDTO(userRepository.findByEmail(email));
    }

    public UserResponseDTO getUserByPhoneNumber(String phoneNumber) {
        return mapper.toDTO(userRepository.findByPhoneNumber(phoneNumber));
    }
}
