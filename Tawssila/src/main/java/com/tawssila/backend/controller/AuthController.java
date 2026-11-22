package com.tawssila.backend.controller;

import com.tawssila.backend.dto.AuthResponse;
import com.tawssila.backend.dto.LoginRequest;
import com.tawssila.backend.dto.RegisterRequest;
import com.tawssila.backend.dto.UserResponse;
import com.tawssila.backend.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public UserResponse me(Authentication authentication) {
        return authService.me(authentication.getName());
    }
}
