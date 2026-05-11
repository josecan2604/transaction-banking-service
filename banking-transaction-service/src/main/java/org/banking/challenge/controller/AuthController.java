package org.banking.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.banking.challenge.config.JwtConfig;
import org.banking.challenge.dtos.CreateUserRequest;
import org.banking.challenge.dtos.CreateUserResponse;
import org.banking.challenge.dtos.LoginRequest;
import org.banking.challenge.dtos.LoginResponse;
import org.banking.challenge.entities.User;
import org.banking.challenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtService;

    @PostMapping("/token")
    public LoginResponse generateToken(
            @RequestBody LoginRequest request
    ) {

        User user = userRepository.findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new RuntimeException("Invalid credentials")
                );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getUsername()
        );

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    @PostMapping("/register")
    public CreateUserResponse register(
            @RequestBody CreateUserRequest request
    ) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        userRepository.save(user);

        return new CreateUserResponse(
                "User created successfully"
        );
    }
}