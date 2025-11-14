package com.rental.manager.web.controller;

import com.rental.manager.dto.request.UserRequest;
import com.rental.manager.dto.response.UserResponse;
import com.rental.manager.entities.User;
import com.rental.manager.enums.UserRole;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody @Validated UserRequest request) {
        User user = new User(request.getName(), request.getPhoneNumber(), request.getEmail(), request.getRole());
        User savedUser = userService.createUser(user);

        return mapToResponse(savedUser);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(mapToResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public List<UserResponse> getUserByRole(@PathVariable UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody @Validated UserRequest request) {
        try {
            User userDetails = new User(request.getName(), request.getPhoneNumber(),
                    request.getEmail(), request.getRole());
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(mapToResponse(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
