package com.lib.mg.controller;

import com.lib.mg.Dto.AuthRequestDto;
import com.lib.mg.Dto.RefreshTokenRequestDto;
import com.lib.mg.Dto.RegisterRequestDto;
import com.lib.mg.Dto.TokenResponseDto;
import com.lib.mg.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto register(@RequestBody RegisterRequestDto requestDto) {
        return authService.register(requestDto);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody AuthRequestDto requestDto) {
        return authService.login(requestDto);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        return authService.refreshToken(requestDto);
    }

    @PostMapping("/logout/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable Long userId) {
        authService.logout(userId);
    }
}
