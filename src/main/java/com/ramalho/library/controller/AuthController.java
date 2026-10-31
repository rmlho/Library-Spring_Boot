package com.ramalho.library.controller;

import com.ramalho.library.config.TokenProvider;
import com.ramalho.library.database.model.UsersEntity;
import com.ramalho.library.dto.UsersRequestDTO;
import com.ramalho.library.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void loginUsers(@RequestBody @Valid UsersRequestDTO usersRequestDTO) {
        authService.registerUsers(usersRequestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUsers(@RequestBody @Valid UsersRequestDTO usersRequestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(usersRequestDTO.getEmail(), usersRequestDTO.getPassword());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenProvider.generationToken(auth.getName());

        return ResponseEntity.ok(token);
    }
}
