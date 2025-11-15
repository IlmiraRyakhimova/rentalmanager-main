package com.rental.manager.web.controller;

import com.rental.manager.dto.requestdto.UserRequestDTO;
import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.mappers.UserMapper;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Validated UserRequestDTO request) {
        return ResponseEntity.ok(userService.createUser(mapper.toEntity(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
                                                      @RequestBody @Validated UserRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, mapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserByName(name));
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search/{phone-number}")
    public ResponseEntity<UserResponseDTO> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));
    }
}
