package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.*;
import com.rental.manager.dto.responsedto.UserResponseDto;
import com.rental.manager.entities.User;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rental.manager.mappers.UserMapper;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ENTITY_NOT_FOUND_MSG = "Пользователь не найден с id: ";

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        User user = mapper.toEntity(request);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto patchUser(UUID id, UserPatchRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        mapper.updateEntity(user, request);
        return mapper.toDto(userRepository.save(user));
    }



    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        return mapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id)));
    }

    @Override
    public List<UserResponseDto> getUserByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(mapper::toDto).toList();
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        return mapper.toDto(userRepository.findByEmail(email));
    }

    public UserResponseDto getUserByPhoneNumber(String phoneNumber) {
        return mapper.toDto(userRepository.findByPhoneNumber(phoneNumber));
    }

    public void changePassword(ChangePasswordRequestDto request) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new BadCredentialsException("Неверный пароль.");
        }

        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от текущего.");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    public void changePhoneNumber(ChangePhoneNumberRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUserEmail);

        currentUser.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(currentUser);
    }

    public void changeUserName(ChangeUserNameRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUserEmail);

        currentUser.setName(request.getUserName());
        userRepository.save(currentUser);
    }
}
