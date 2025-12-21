package com.rental.manager.web.controller;

import com.rental.manager.dto.requestdto.ChangePasswordRequestDto;
import com.rental.manager.dto.requestdto.ChangePhoneNumberRequest;
import com.rental.manager.dto.requestdto.ChangeUserNameRequest;
import com.rental.manager.service.UserService;
import liquibase.change.Change;
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

    @PostMapping("/change-phone-number")
    ResponseEntity<String> changePhoneNumber(@RequestBody @Validated ChangePhoneNumberRequest request) {
        userService.changePhoneNumber(request);
        return ResponseEntity.ok("Номер телефона изменен");
    }

    @PostMapping("/change-user-name")
    ResponseEntity<String> changeUserName(@RequestBody ChangeUserNameRequest request) {
        userService.changeUserName(request);
        return ResponseEntity.ok("Имя изменено");
    }

}

