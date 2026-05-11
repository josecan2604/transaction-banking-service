package org.banking.challenge.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(
        name = "Authentication",
        description = "Authentication APIs"
)
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtService;

    @Operation(
            summary = "Generate JWT token",
            description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Token generated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = LoginResponse.class
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            )
    })
    @PostMapping("/token")
    public LoginResponse generateToken(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "username": "jarana",
                                  "password": "1234"
                                }
                                """
                            )
                    )
            )

            @RequestBody
            LoginRequest request
    ) {

        User user = userRepository.findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid credentials"
                        )
                );

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        String token = jwtService.generateToken(
                user.getUsername()
        );

        LoginResponse response =
                LoginResponse.builder()
                        .token(token)
                        .build();

        return
                        response;
    }

    @Operation(
            summary = "Register user",
            description = "Creates a new user account"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateUserResponse.class
                            )
                    )
            )
    })
    @PostMapping("/register")
    public CreateUserResponse createUser(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create user request",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "username": "jarana",
                                      "password": "1234"
                                    }
                                    """
                            )
                    )
            )

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