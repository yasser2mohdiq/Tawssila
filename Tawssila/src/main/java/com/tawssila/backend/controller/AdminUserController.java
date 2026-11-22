package com.tawssila.backend.controller;

import com.tawssila.backend.dto.CreateUserRequest;
import com.tawssila.backend.dto.UserResponse;
import com.tawssila.backend.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return authService.createUser(request);
    }
}
