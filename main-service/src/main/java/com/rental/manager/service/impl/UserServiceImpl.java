package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.ChangePasswordRequestDTO;
import com.rental.manager.dto.requestdto.UserPatchRequestDTO;
import com.rental.manager.dto.requestdto.UserRequestDTO;
import com.rental.manager.dto.responsedto.UserResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ENTITY_NOT_FOUND_MSG = "User not found with id: ";

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = mapper.toEntity(request);
        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO patchUser(UUID id, UserPatchRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        return mapper.toDTO(userRepository.save(user));
    }



    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        return mapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id)));
    }

    @Override
    public List<UserResponseDTO> getUserByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(mapper::toDTO).toList();
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return mapper.toDTO(userRepository.findByEmail(email));
    }

    public UserResponseDTO getUserByPhoneNumber(String phoneNumber) {
        return mapper.toDTO(userRepository.findByPhoneNumber(phoneNumber));
    }

    public void changePassword(ChangePasswordRequestDTO request) {
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
}
