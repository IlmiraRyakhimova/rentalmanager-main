package com.rental.manager.web.controller;

import com.rental.manager.dto.requestdto.ChangePasswordRequestDto;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/account-settings")
@RequiredArgsConstructor
public class AccountSettingsController {
    private final UserService userService;

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody @Validated ChangePasswordRequestDto request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Пароль изменен");
    }
}
